/*
 * Xml Doclet- Plugs into Javadoc & produces documentation in XML instead of HTML.  One can process XML into a better HTML
 * or other forms of documentation than Javadoc itself produces.
 *
 * @Author: dineshv
 * Date: 4/26/11
 * Time: 10:23 AM
 *
 * Copyright (c) 2011, Dinesh Visweswaraiah. dinesh (at) dineshv com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package com.dineshv.XmlDoclet
import com.sun.javadoc._
import xml.{PrettyPrinter, Text, Elem}
import collection.mutable.ListBuffer

trait XmlMethods {
  def toXml : List[Elem]
  def Type(in:Elem*) : Elem
  def annotationTypeElementDoc (ated:AnnotationTypeElementDoc): Elem = {
    <ated><fqn>{ated.qualifiedName}</fqn><defaultValue>{ated.defaultValue}</defaultValue></ated>
  }

  def annotationTypeDoc2Xml (adt:AnnotationTypeDoc):Elem = {
    <annotationType>
      <fqn>{adt.qualifiedName}</fqn>
      {if (adt.elements.length > 0) <elements>{for (ated <- adt.elements) yield <element>{annotationTypeElementDoc(ated)}</element>}</elements>}
    </annotationType>
  }
  def annotation2Xml (ad:AnnotationDesc):Elem = {
    <annotation>
      {annotationTypeDoc2Xml(ad.annotationType)}
      {if (ad.elementValues.length > 0) for (ev <- ad.elementValues) yield <element>{annotationTypeElementDoc(ev.element)}<value>{ev.value}</value></element>}
    </annotation>
  }
  def innards2Xml : ListBuffer[Elem]
  def pos2XML(pos:SourcePosition): Elem = <pos><l>{pos.line}</l></pos>
  def modifiers(d:ProgramElementDoc):Elem={
    var l = ListBuffer[Elem] ({
      if (d.isPublic) <public/> else
        if (d.isProtected) <protected/> else
          if (d.isPrivate) <private/> else <package-private/>
    })
    if (d.isStatic) l += <static/>
    if (d.isFinal) l += <final/>
    if (d.annotations.length > 0)
      l += <annotations>{for (a <- d.annotations) yield {annotation2Xml(a)}}</annotations>
    l = l ++ extraModifiers()
    <modifiers>
      {l}
    </modifiers>
  }

  def extraModifiers():ListBuffer[Elem]

  def tag2Xml (t: Tag):Elem = {
    if (t.name.indexOf('@') != -1) Elem(null, t.name.substring(1, t.name.length-1), null, scala.xml.TopScope, Text(t.text))
    else Elem(null, t.name, null, scala.xml.TopScope, Text(t.text))
  }
  def tags2Xml (tags:Array[Tag]): Array[Elem] = {
    for (t <- tags) yield tag2Xml(t)
  }
  def type2Xml (t:Type):Elem = {
    if ("" != t.dimension) {
      <array><dim>{t.dimension.count((c) => (c=='['))}</dim><type>{t.typeName}</type></array>
    } else <type>{t}</type>
  }
  def params2xml (p:Parameter):Elem = {
    <parameter><name>{p.name}</name><type>{type2Xml(p.`type`)}</type></parameter>
  }
}

abstract class SPEDoc (d:ProgramElementDoc) extends XmlMethods {
  override def toXml : List[Elem] = {
    var l = ListBuffer[Elem]()
    if (d.firstSentenceTags.length > 0) l += <fs>{tags2Xml(d.firstSentenceTags)}</fs>
    if (d.tags.length > 0)  l += <rest>{tags2Xml(d.tags)}</rest>
    List[Elem](Type(innards2Xml:_*), <comment>{l}</comment>)
  }

  override def innards2Xml: ListBuffer[Elem] = {
    ListBuffer[Elem](<name>{d.name}</name>, pos2XML(d.position), modifiers(d))
  }
}

class SMethodDoc (md:MethodDoc) extends SPEDoc(md) {

  override def extraModifiers():ListBuffer[Elem] = {
    var l = ListBuffer[Elem]()
    if (md.isAbstract) l += <abstract/>
    if (md.isSynchronized) l += <synchronized/>
    if (md.isSynthetic) l += <synthetic/>
    if (md.isVarArgs) l += <varArgs/>
    if (md.isNative) l += <native/>
    l += <returns>{type2Xml(md.returnType)}</returns>
    if (md.thrownExceptions.length > 0) {
      for (e <- md.thrownExceptions) l  += <throws><name>{e.name}</name><fqn>{e.qualifiedName}</fqn></throws>
    }
    l += <signature>{md.signature}</signature>

    l
  }
  def Type(in:Elem*) : Elem = <method>{in}</method>
  override def innards2Xml: ListBuffer[Elem] = {
    var l = super.innards2Xml
    if (md.parameters.length > 0) l += <parameters>{for (p <- md.parameters) yield params2xml(p)}</parameters>
    l
  }
}

class SConstructorDoc (md:ConstructorDoc) extends SPEDoc(md) {
  override def extraModifiers():ListBuffer[Elem] = {
    var l = ListBuffer[Elem]()
    if (md.isSynchronized) l += <synchronized/>
    if (md.isSynthetic) l += <synthetic/>
    if (md.isVarArgs) l += <varArgs/>
    l
  }

  def Type(in:Elem*) : Elem = <constructor>{in}</constructor>
  override def innards2Xml: ListBuffer[Elem] = {
    var l = super.innards2Xml
    if (md.parameters.length > 0) l += <parameters>{for (p <- md.parameters) yield params2xml(p)}</parameters>
    l
  }
}

class SFieldDoc (fd:FieldDoc) extends SPEDoc(fd) {
  override def innards2Xml: ListBuffer[Elem] = {
    var l = super.innards2Xml += type2Xml (fd.`type`)
    if (fd.constantValue != null) l += <constantValue>{fd.constantValue}</constantValue>
    l
  }

  override def extraModifiers():ListBuffer[Elem] = {
    var l = ListBuffer[Elem]()
    if (fd.isTransient) l += <transient/>
    if (fd.isVolatile) l += <volatile/>
    l
  }
  def Type(in:Elem*) : Elem = <field>{in}</field>
}

class SClassDoc(cd:ClassDoc) extends SPEDoc(cd) {
  import XmlDoclet._

  override def Type(in:Elem*) : Elem = if (cd.isEnum) <enum>{in}</enum>
    else <class>{in}</class>

  override def extraModifiers():ListBuffer[Elem] = {
    var l = ListBuffer[Elem]()
    if (cd.isAbstract) l += <abstract/>
    if (cd.interfaces.length > 0) {
      for (i <- cd.interfaces) {
        l += <implements><name>{i.name}</name><fqn>{i.qualifiedName}</fqn></implements>
      }
    }
    if ((cd.superclass != null) && (cd.superclass.qualifiedName != "java.lang.Object")) l += <extends><name>{cd.superclass.name}</name><fqn>{cd.superclass.qualifiedName}</fqn></extends>

    l
  }

  override def innards2Xml : ListBuffer[Elem] = {
    var l = ListBuffer[Elem](<name>{cd.containingPackage.name}</name>, <fqn>{cd.qualifiedName}</fqn>)
    l ++= super.innards2Xml
    if (cd.fields.length > 0) l += <fields>{for (f:FieldDoc <- cd.fields()) yield f.toXml}</fields>
    if (cd.constructors.length > 0) l += <constructors>{for (c:ConstructorDoc <- cd.constructors) yield c.toXml}</constructors>
    if (cd.methods.length > 0) l += <methods>{for (m:MethodDoc <- cd.methods) yield m.toXml}</methods>
    if (cd.isEnum) l += <enumConstants>{for (e:FieldDoc <- cd.enumConstants) yield e.toXml}</enumConstants>
    if (cd.innerClasses.length > 0) l += <innerClasses>{for (ic <- cd.innerClasses) yield ic.toXml}</innerClasses>
    l
  }

  override def toXml : List[Elem] = {
    val x:Elem =
    <java>
      <source-file>{cd.position.file.getPath}</source-file>
      <package>
        {Type(innards2Xml: _*)}
      </package>
    </java>
    List(x)
  }
}

object XmlDoclet {
import com.sun.javadoc._
  implicit def classDoc2SClassDoc (d:ClassDoc) : SClassDoc = {new SClassDoc(d)}
  implicit def methodDoc2SMethodDoc (d:MethodDoc) : SMethodDoc = new SMethodDoc(d)
  implicit def fieldDoc2SMethodDoc (d:FieldDoc) : SFieldDoc = new SFieldDoc(d)
  implicit def constructorDoc2SConstructorDoc (d:ConstructorDoc) : SConstructorDoc = new SConstructorDoc(d)

  def start (rootDoc : RootDoc): Boolean = {
    val prettyPrinter = new PrettyPrinter(120, 4)

    val xmlDoc = List[List[Elem]]({for (c <- rootDoc.classes) yield c.toXml}: _*)

    println("Printing generated XML")
    xmlDoc.map(c => {

      for (x <- c) {

        if (x != null) {
          println (x)
          //println (prettyPrinter.fo x)
        }

      }
    })
    true
  }
}