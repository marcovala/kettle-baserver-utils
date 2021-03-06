/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.di.baserver.utils.widgets;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.pentaho.di.core.Const;
import org.pentaho.di.ui.core.PropsUI;

/**
 * @author Marco Vala
 */
public abstract class WidgetBuilder<T extends Control> {

  //region Fields

  private T widget;
  protected Composite parent;
  protected PropsUI props;
  private Control top = null;
  private Control left = null;
  private int width;
  private int height;
  private int topPlacement = -1;
  private int leftPlacement = -1;
  private int rightPlacement = -1;
  private int bottomPlacement = -1;
  private int topMargin = Const.MARGIN;
  private int leftMargin = Const.MARGIN;
  private int rightMargin = Const.MARGIN;
  private int bottomMargin = Const.MARGIN;
  private boolean isEnabled = true;

  //endregion

  //region Getters / Setters

  public T getWidget() {
    return this.widget;
  }

  public void setWidget( T widget ) {
    this.widget = widget;
  }

  public Composite getParent() {
    return this.parent;
  }

  public void setParent( Composite parent ) {
    this.parent = parent;
  }

  public PropsUI getProps() {
    return this.props;
  }

  public void setProps( PropsUI props ) {
    this.props = props;
  }

  public Control getTop() {
    return top;
  }

  public WidgetBuilder<T> setTop( Control top ) {
    this.top = top;
    return this;
  }

  public Control getLeft() {
    return left;
  }

  public WidgetBuilder<T> setLeft( Control left ) {
    this.left = left;
    return this;
  }

  public int getWidth() {
    return width;
  }

  public WidgetBuilder<T> setWidth( int width ) {
    this.width = width;
    return this;
  }

  public int getHeight() {
    return height;
  }

  public WidgetBuilder<T> setHeight( int height ) {
    this.height = height;
    return this;
  }

  public int getTopPlacement() {
    return topPlacement;
  }

  public WidgetBuilder<T> setTopPlacement( int topPlacement ) {
    this.topPlacement = topPlacement;
    return this;
  }

  public int getLeftPlacement() {
    return leftPlacement;
  }

  public WidgetBuilder<T> setLeftPlacement( int leftPlacement ) {
    this.leftPlacement = leftPlacement;
    return this;
  }

  public int getRightPlacement() {
    return rightPlacement;
  }

  public WidgetBuilder<T> setRightPlacement( int rightPlacement ) {
    this.rightPlacement = rightPlacement;
    return this;
  }

  public int getBottomPlacement() {
    return bottomPlacement;
  }

  public WidgetBuilder<T> setBottomPlacement( int bottomPlacement ) {
    this.bottomPlacement = bottomPlacement;
    return this;
  }

  public int getTopMargin() {
    return topMargin;
  }

  public WidgetBuilder<T> setTopMargin( int topMargin ) {
    this.topMargin = topMargin;
    return this;
  }

  public int getLeftMargin() {
    return leftMargin;
  }

  public WidgetBuilder<T> setLeftMargin( int leftMargin ) {
    this.leftMargin = leftMargin;
    return this;
  }

  public int getRightMargin() {
    return rightMargin;
  }

  public WidgetBuilder<T> setRightMargin( int rightMargin ) {
    this.rightMargin = rightMargin;
    return this;
  }

  public int getBottomMargin() {
    return bottomMargin;
  }

  public WidgetBuilder<T> setBottomMargin( int bottomMargin ) {
    this.bottomMargin = bottomMargin;
    return this;
  }

  public boolean isEnabled() {
    return this.isEnabled;
  }

  public WidgetBuilder<T> setEnabled( boolean isEnabled ) {
    this.isEnabled = isEnabled;
    return this;
  }

  //endregion

  //region Constructors

  protected WidgetBuilder( Composite parent, PropsUI props ) {
    this.parent = parent;
    this.props = props;
  }

  //endregion

  //region Methods

  public T build() {
    this.widget = createWidget( this.parent );
    this.props.setLook( this.widget );
    placeWidget( this.widget );
    this.widget.setEnabled( this.isEnabled );
    return this.widget;
  }

  protected abstract T createWidget( Composite parent );

  protected void placeWidget( Control widget ) {
    FormData data = new FormData();
    if ( this.topPlacement >= 0 ) {
      data.top = new FormAttachment( this.topPlacement, this.topMargin );
    } else if ( this.top != null ) {
      data.top = new FormAttachment( this.top, this.topMargin );
    }
    if ( this.leftPlacement >= 0 ) {
      data.left = new FormAttachment( this.leftPlacement, this.leftMargin );
    } else if ( this.left != null ) {
      data.left = new FormAttachment( this.left, this.leftMargin );
    }
    if ( this.rightPlacement >= 0 ) {
      data.right = new FormAttachment( this.rightPlacement, -this.rightMargin );
    } else if ( this.width > 0 ) {
      data.width = this.width;
    }
    if ( this.bottomPlacement >= 0 ) {
      data.bottom = new FormAttachment( this.bottomPlacement, -this.bottomMargin );
    } else if ( this.height > 0 ) {
      data.height = this.height;
    }
    widget.setLayoutData( data );
  }

  //endregion
}
