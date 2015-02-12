/*
 *  Copyright 2002 - 2015 Webdetails, a Pentaho company.  All rights reserved.
 *
 *  This software was developed by Webdetails and is provided under the terms
 *  of the Mozilla Public License, Version 2.0, or any later version. You may not use
 *  this file except in compliance with the license. If you need a copy of the license,
 *  please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
 *
 *  Software distributed under the Mozilla Public License is distributed on an "AS IS"
 *  basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 *  the license for the specific language governing your rights and limitations.
 */

package pt.webdetails.di.baserver.utils.repositoryPlugin.ui;

import org.apache.commons.vfs.FileObject;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.metastore.api.exceptions.MetaStoreException;
import org.pentaho.metastore.persist.MetaStoreFactory;
import org.pentaho.metastore.util.PentahoDefaults;
import org.pentaho.vfs.ui.VfsFileChooserDialog;
import pt.webdetails.di.baserver.utils.repositoryPlugin.Constants;
import pt.webdetails.di.baserver.utils.repositoryPlugin.IPentahoConnectionConfiguration;
import pt.webdetails.di.baserver.utils.repositoryPlugin.PentahoConnectionConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class PentahoSolutionVfsFileChooserController {

  private static MetaStoreFactory<PentahoConnectionConfiguration> metaStoreFactory =
    new MetaStoreFactory<PentahoConnectionConfiguration>( PentahoConnectionConfiguration.class,
      Spoon.getInstance().getMetaStore(), PentahoDefaults.NAMESPACE );




  // region Properties

  public PentahoSolutionVfsFileChooserPanel getView() {
    return this.view;
  }
  protected PentahoSolutionVfsFileChooserController setView( PentahoSolutionVfsFileChooserPanel view ) {
    this.view = view;
    return this;
  }
  private PentahoSolutionVfsFileChooserPanel view;

  // endregion

  // region Constructors

  public PentahoSolutionVfsFileChooserController( PentahoSolutionVfsFileChooserPanel view ) {
    this.setView( view );
    this.updateConnectionsDropDown( view );
    this.addConnectButtonListener( view );
    this.addEditConnectionButtonListener( view );
    this.addDeleteConnectionButtonListener( view );
    this.addNewConnectionButtonListener( view );
  }

  // constructor for unit tests
  protected PentahoSolutionVfsFileChooserController() { }

  // endregion

  // region Methods
  private FileObject getFileObject( String vfsFileUri ) throws KettleFileException {
    FileObject file;
    file = KettleVFS.getFileObject( vfsFileUri );
    return file;
  }

  /***
   *
   * @return the file URI constructed from the dialog input
   */
  protected String getPentahoConnectionString( String vfsScheme, URL serverUrl, String username, String password ) {
    StringBuilder urlString = new StringBuilder( vfsScheme );
    urlString.append( ":" );

    urlString.append( serverUrl.getProtocol() );
    urlString.append( "://" );

    if ( !nullOrEmpty( username ) ) {
      urlString.append( username );
      urlString.append( ":" );
      urlString.append( password );
      urlString.append( "@" );
    }

    urlString.append( serverUrl.getHost() );
    int port = serverUrl.getPort();
    if ( port != -1 ) { // if port is specified
      urlString.append( ":" );
      urlString.append( port );
    }

    urlString.append( serverUrl.getPath() );

    return urlString.toString();
  }

  /***
   * Shows a message box to the user
   * @param message
   * @param shell
   */
  private void showMessage( String message, Shell shell ) {
    MessageBox box = new MessageBox( shell );
    box.setText( "BOX TEXT" ); //$NON-NLS-1$
    box.setMessage( message );
    box.open();
  }



  protected void updateConnectionsDropDown( PentahoSolutionVfsFileChooserPanel view ) {
    List<String> configurationNames = this.getConfigurationNames();
    if ( configurationNames.size() > 0 ) {
      String[] namesArray = configurationNames.toArray( new String[ configurationNames.size() ] );
      Const.sortStrings( namesArray );
      view.getConnectionsDropDown().setItems( namesArray );
      view.getConnectionsDropDown().setText( namesArray[ 0 ] );
    } else {
      view.getConnectionsDropDown().setItems( new String[0] );
      view.getConnectionsDropDown().setText( "" );
    }
  }


  protected void addConnectButtonListener( PentahoSolutionVfsFileChooserPanel view ) {

    view.getConnectButton().addSelectionListener( new SelectionListener() {
      @Override
      public void widgetSelected( SelectionEvent selectionEvent ) {
        connect();
      }

      @Override
      public void widgetDefaultSelected( SelectionEvent selectionEvent ) {

      }
    } );

  }

  /***
   * Connects to the Pentaho repository specified by the information
   * in the view associated with the controller
   */
  protected void connect() {
    String configurationName = this.getView().getConnectionsDropDown().getText();
    IPentahoConnectionConfiguration configuration = this.getConfiguration( configurationName );

    showMessage( configurationName, this.getView().getShell() );

    try {
      URL serverUrl = new URL( configuration.getServerUrl() );
      String userName = configuration.getUserName();
      String password = configuration.getPassword();
      String vfsScheme = Constants.getInstance().getVfsScheme();
      String connectionString = this.getPentahoConnectionString( vfsScheme, serverUrl, userName, password );

      FileObject file = this.getFileObject( connectionString );
      VfsFileChooserDialog vfsFileChooserDialog = this.getView().getVfsFileChooserDialog();
      vfsFileChooserDialog.setSelectedFile( file );
      vfsFileChooserDialog.setRootFile( file );

    } catch ( MalformedURLException e ) {
      showMessage( "ERROR URL", this.getView().getShell() );
    } catch ( KettleFileException e ) {
      showMessage( "ERROR FILE", this.getView().getShell() );
    }
  }

  protected void addEditConnectionButtonListener( PentahoSolutionVfsFileChooserPanel view ) {

    view.getEditConnectionButton().addSelectionListener( new SelectionListener() {
      @Override
      public void widgetSelected( SelectionEvent selectionEvent ) {
        editConnection();
      }

      @Override
      public void widgetDefaultSelected( SelectionEvent selectionEvent ) {

      }
    } );

  }

  protected void editConnection() {
  }

  protected void addDeleteConnectionButtonListener( PentahoSolutionVfsFileChooserPanel view ) {

    view.getDeleteConnectionButton().addSelectionListener( new SelectionListener() {
      @Override
      public void widgetSelected( SelectionEvent selectionEvent ) {
        deleteConnection();
      }

      @Override
      public void widgetDefaultSelected( SelectionEvent selectionEvent ) {

      }
    } );

  }

  protected void deleteConnection() {

    String configurationName = this.getView().getConnectionsDropDown().getText();
    this.deleteConfiguration( configurationName );

    if ( this.getConfiguration( configurationName ) == null ) {
      showMessage( configurationName + " deleted!", this.getView().getShell() );

      this.updateConnectionsDropDown( this.getView() );
    }



  }

  protected void addNewConnectionButtonListener( final PentahoSolutionVfsFileChooserPanel view ) {

    view.getNewConnectionButton().addSelectionListener( new SelectionListener() {
      @Override public void widgetSelected( SelectionEvent selectionEvent ) {
        newConnection();
      }

      @Override public void widgetDefaultSelected( SelectionEvent selectionEvent ) {

      }
    } );

  }

  protected void newConnection() {
    PentahoConnectionConfiguration configuration = new PentahoConnectionConfiguration()
      .setName( "amazing" );

    Shell openUrlShell = this.getView().getVfsFileChooserDialog().dialog;
    PentahoConnectionConfigurationDialog dialog = new PentahoConnectionConfigurationDialog(  openUrlShell , configuration );
    PentahoConnectionConfigurationDialogController controller = new PentahoConnectionConfigurationDialogController( dialog );

    dialog.open();
  }

  private List<String> getConfigurationNames() {
    try {
      return metaStoreFactory.getElementNames();
    } catch ( MetaStoreException e ) {
      return Collections.emptyList();
    }
  }

  private IPentahoConnectionConfiguration getConfiguration( final String configurationName ) {
    try {
      return metaStoreFactory.loadElement( configurationName );
    } catch ( MetaStoreException e ) {
      return null;
    }
  }

  private void deleteConfiguration( final String configurationName ) {
    try {
      metaStoreFactory.deleteElement( configurationName );
    } catch ( MetaStoreException e ) {
      // do nothing?
    }
  }


  // region aux
  private static boolean nullOrEmpty( String string ) {
    return string == null || string.isEmpty();
  }
  // endregion
  // endregion
}
