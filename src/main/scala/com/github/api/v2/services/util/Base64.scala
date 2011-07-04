/*
 * Copyright 2010 Nabeel Mukhtar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.api.v2.services.util
import scala.util.control.Breaks.{breakable,break}

/**
 * The Class Base64.
 */
object Base64 {
  /**
   * Gets the alphabet.
   *
   * @param options
   *            the options
   *
   * @return the alphabet
   */
  private final def getAlphabet(options: Int): Array[Byte] = {
    if ((options & URL_SAFE) == URL_SAFE) {
      return _URL_SAFE_ALPHABET
    }
    else if ((options & ORDERED) == ORDERED) {
      return _ORDERED_ALPHABET
    }
    else {
      return _STANDARD_ALPHABET
    }
  }

  /**
   * Gets the decodabet.
   *
   * @param options
   *            the options
   *
   * @return the decodabet
   */
  private final def getDecodabet(options: Int): Array[Byte] = {
    if ((options & URL_SAFE) == URL_SAFE) {
      return _URL_SAFE_DECODABET
    }
    else if ((options & ORDERED) == ORDERED) {
      return _ORDERED_DECODABET
    }
    else {
      return _STANDARD_DECODABET
    }
  }

  /**
   * Encode3to4.
   *
   * @param b4
   *            the b4
   * @param threeBytes
   *            the three bytes
   * @param numSigBytes
   *            the num sig bytes
   * @param options
   *            the options
   *
   * @return the byte[]
   */
  private def encode3to4(b4: Array[Byte], threeBytes: Array[Byte], numSigBytes: Int, options: Int): Array[Byte] = {
    encode3to4(threeBytes, 0, numSigBytes, b4, 0, options)
    return b4
  }

  /**
   * Encode3to4.
   *
   * @param source
   *            the source
   * @param srcOffset
   *            the src offset
   * @param numSigBytes
   *            the num sig bytes
   * @param destination
   *            the destination
   * @param destOffset
   *            the dest offset
   * @param options
   *            the options
   *
   * @return the byte[]
   */
  private def encode3to4(source: Array[Byte], srcOffset: Int, numSigBytes: Int, destination: Array[Byte], destOffset: Int, options: Int): Array[Byte] = {
    var ALPHABET: Array[Byte] = getAlphabet(options)
    var inBuff: Int = (if (numSigBytes > 0) ((source(srcOffset) << 24) >>> 8) else 0) | (if (numSigBytes > 1) ((source(srcOffset + 1) << 24) >>> 16) else 0) | (if (numSigBytes > 2) ((source(srcOffset + 2) << 24) >>> 24) else 0)
    numSigBytes match {
      case 3 =>
        destination(destOffset) = ALPHABET((inBuff >>> 18))
        destination(destOffset + 1) = ALPHABET((inBuff >>> 12) & 0x3f)
        destination(destOffset + 2) = ALPHABET((inBuff >>> 6) & 0x3f)
        destination(destOffset + 3) = ALPHABET((inBuff) & 0x3f)
        return destination
      case 2 =>
        destination(destOffset) = ALPHABET((inBuff >>> 18))
        destination(destOffset + 1) = ALPHABET((inBuff >>> 12) & 0x3f)
        destination(destOffset + 2) = ALPHABET((inBuff >>> 6) & 0x3f)
        destination(destOffset + 3) = EQUALS_SIGN
        return destination
      case 1 =>
        destination(destOffset) = ALPHABET((inBuff >>> 18))
        destination(destOffset + 1) = ALPHABET((inBuff >>> 12) & 0x3f)
        destination(destOffset + 2) = EQUALS_SIGN
        destination(destOffset + 3) = EQUALS_SIGN
        return destination
      case _ =>
        return destination
    }
  }

  /**
   * Encode.
   *
   * @param raw
   *            the raw
   * @param encoded
   *            the encoded
   */
  def encode(raw: java.nio.ByteBuffer, encoded: java.nio.ByteBuffer): Unit = {
    var raw3 = new Array[Byte](3)
    var enc4 = new Array[Byte](4)
    while (raw.hasRemaining) {
      var rem: Int = Math.min(3, raw.remaining)
      raw.get(raw3, 0, rem)
      Base64.encode3to4(enc4, raw3, rem, Base64.NO_OPTIONS)
      encoded.put(enc4)
    }
  }

  /**
   * Encode.
   *
   * @param raw
   *            the raw
   * @param encoded
   *            the encoded
   */
  def encode(raw: java.nio.ByteBuffer, encoded: java.nio.CharBuffer): Unit = {
    var raw3 = new Array[Byte](3)
    var enc4 = new Array[Byte](4)
    while (raw.hasRemaining) {
      var rem: Int = Math.min(3, raw.remaining)
      raw.get(raw3, 0, rem)
      Base64.encode3to4(enc4, raw3, rem, Base64.NO_OPTIONS)

      var i = 0
      while (i < 4) {
        encoded.put((enc4(i) & 0xFF).asInstanceOf[Char])
        i += 1
      }
    }
  }

  /**
   * Encode object.
   *
   * @param serializableObject
   *            the serializable object
   *
   * @return the string
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeObject(serializableObject: Serializable): String = {
    return encodeObject(serializableObject, NO_OPTIONS)
  }

  /**
   * Encode object.
   *
   * @param serializableObject
   *            the serializable object
   * @param options
   *            the options
   *
   * @return the string
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeObject(serializableObject: java.io.Serializable, options: Int): String = {
    if (serializableObject == null) {
      throw new NullPointerException("Cannot serialize a null object.")
    }
    var baos: java.io.ByteArrayOutputStream = null
    var b64os: java.io.OutputStream = null
    var gzos: java.util.zip.GZIPOutputStream = null
    var oos: java.io.ObjectOutputStream = null
    try {
      baos = new java.io.ByteArrayOutputStream
      b64os = new Base64.OutputStream(baos, ENCODE | options)
      if ((options & GZIP) != 0) {
        gzos = new java.util.zip.GZIPOutputStream(b64os)
        oos = new java.io.ObjectOutputStream(gzos)
      }
      else {
        oos = new java.io.ObjectOutputStream(b64os)
      }
      oos.writeObject(serializableObject)
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
    }
    finally {
      try { oos.close  } catch { case e: Exception => {} }
      try { gzos.close } catch { case e: Exception => {} }
      try { b64os.close} catch { case e: Exception => {} }
      try { baos.close } catch { case e: Exception => {} }
    }
    try {
      return new String(baos.toByteArray, PREFERRED_ENCODING)
    }
    catch {
      case uue: java.io.UnsupportedEncodingException => {
        return new String(baos.toByteArray)
      }
    }
  }

  /**
   * Encode bytes.
   *
   * @param source
   *            the source
   *
   * @return the string
   */
  def encodeBytes(source: Array[Byte]): String = {
    var encoded: String = null
    try {
      encoded = encodeBytes(source, 0, source.length, NO_OPTIONS)
    }
    catch {
      case ex: java.io.IOException => {
        assert(false, ex.getMessage)
      }
    }
    assert(encoded != null)
    return encoded
  }

  /**
   * Encode bytes.
   *
   * @param source
   *            the source
   * @param options
   *            the options
   *
   * @return the string
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeBytes(source: Array[Byte], options: Int): String = {
    return encodeBytes(source, 0, source.length, options)
  }

  /**
   * Encode bytes.
   *
   * @param source
   *            the source
   * @param off
   *            the off
   * @param len
   *            the len
   *
   * @return the string
   */
  def encodeBytes(source: Array[Byte], off: Int, len: Int): String = {
    var encoded: String = null
    try {
      encoded = encodeBytes(source, off, len, NO_OPTIONS)
    }
    catch {
      case ex: java.io.IOException => {
        assert(false, ex.getMessage)
      }
    }
    assert(encoded != null)
    return encoded
  }

  /**
   * Encode bytes.
   *
   * @param source
   *            the source
   * @param off
   *            the off
   * @param len
   *            the len
   * @param options
   *            the options
   *
   * @return the string
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeBytes(source: Array[Byte], off: Int, len: Int, options: Int): String = {
    var encoded: Array[Byte] = encodeBytesToBytes(source, off, len, options)
    try {
      return new String(encoded, PREFERRED_ENCODING)
    }
    catch {
      case uue: java.io.UnsupportedEncodingException => {
        return new String(encoded)
      }
    }
  }

  /**
   * Encode bytes to bytes.
   *
   * @param source
   *            the source
   *
   * @return the byte[]
   */
  def encodeBytesToBytes(source: Array[Byte]): Array[Byte] = {
    var encoded: Array[Byte] = null
    try {
      encoded = encodeBytesToBytes(source, 0, source.length, Base64.NO_OPTIONS)
    }
    catch {
      case ex: java.io.IOException => {
        assert(false, "IOExceptions only come from GZipping, which is turned off: " + ex.getMessage)
      }
    }
    return encoded
  }

  /**
   * Encode bytes to bytes.
   *
   * @param source
   *            the source
   * @param off
   *            the off
   * @param len
   *            the len
   * @param options
   *            the options
   *
   * @return the byte[]
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeBytesToBytes(source: Array[Byte], off: Int, len: Int, options: Int): Array[Byte] = {
    if (source == null) {
      throw new NullPointerException("Cannot serialize a null array.")
    }
    if (off < 0) {
      throw new IllegalArgumentException("Cannot have negative offset: " + off)
    }
    if (len < 0) {
      throw new IllegalArgumentException("Cannot have length offset: " + len)
    }
    if (off + len > source.length) {
      throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off.asInstanceOf[AnyRef], len.asInstanceOf[AnyRef], source.length.asInstanceOf[AnyRef]))
    }
    if ((options & GZIP) != 0) {
      var baos: java.io.ByteArrayOutputStream = null
      var gzos: java.util.zip.GZIPOutputStream = null
      var b64os: Base64.OutputStream = null
      try {
        baos = new java.io.ByteArrayOutputStream
        b64os = new Base64.OutputStream(baos, ENCODE | options)
        gzos = new java.util.zip.GZIPOutputStream(b64os)
        gzos.write(source, off, len)
        gzos.close
      }
      catch {
        case e: java.io.IOException => {
          throw e
        }
      }
      finally {
        try { gzos.close } catch { case e: Exception => {} }
        try { b64os.close} catch { case e: Exception => {} }
        try { baos.close } catch { case e: Exception => {} }
      }
      return baos.toByteArray
    }
    else {
      var breakLines: Boolean = (options & DO_BREAK_LINES) != 0
      var encLen: Int = (len / 3) * 4 + (if (len % 3 > 0) 4 else 0)
      if (breakLines) {
        encLen += encLen / MAX_LINE_LENGTH
      }
      var outBuff = new Array[Byte](encLen)
      var d = 0
      var e = 0
      var len2 = len - 2
      var lineLength = 0
      while (d < len2) {
        {
          encode3to4(source, d + off, 3, outBuff, e, options)
          lineLength += 4
          if (breakLines && lineLength >= MAX_LINE_LENGTH) {
            outBuff(e + 4) = NEW_LINE
            ({
              e += 1; e
            })
            lineLength = 0
          }
        }
        d += 3
        e += 4
      }
      if (d < len) {
        encode3to4(source, d + off, len - d, outBuff, e, options)
        e += 4
      }
      if (e <= outBuff.length - 1) {
        var finalOut: Array[Byte] = new Array[Byte](e)
        System.arraycopy(outBuff, 0, finalOut, 0, e)
        return finalOut
      }
      else {
        return outBuff
      }
    }
  }

  /**
   * Decode4to3.
   *
   * @param source
   *            the source
   * @param srcOffset
   *            the src offset
   * @param destination
   *            the destination
   * @param destOffset
   *            the dest offset
   * @param options
   *            the options
   *
   * @return the int
   */
  private def decode4to3(source: Array[Byte], srcOffset: Int, destination: Array[Byte], destOffset: Int, options: Int): Int = {
    if (source == null) {
      throw new NullPointerException("Source array was null.")
    }
    if (destination == null) {
      throw new NullPointerException("Destination array was null.")
    }
    if (srcOffset < 0 || srcOffset + 3 >= source.length) {
      throw new IllegalArgumentException("Source array with length %d cannot have offset of %d and still process four bytes.".format(source.length, srcOffset))
    }
    if (destOffset < 0 || destOffset + 2 >= destination.length) {
      throw new IllegalArgumentException("Destination array with length %d cannot have offset of %d and still store three bytes.".format( destination.length, destOffset))
    }
    var DECODABET: Array[Byte] = getDecodabet(options)
    if (source(srcOffset + 2) == EQUALS_SIGN) {
      var outBuff: Int = ((DECODABET(source(srcOffset)) & 0xFF) << 18) | ((DECODABET(source(srcOffset + 1)) & 0xFF) << 12)
      destination(destOffset) = (outBuff >>> 16).asInstanceOf[Byte]
      return 1
    }
    else if (source(srcOffset + 3) == EQUALS_SIGN) {
      var outBuff: Int = ((DECODABET(source(srcOffset)) & 0xFF) << 18) | ((DECODABET(source(srcOffset + 1)) & 0xFF) << 12) | ((DECODABET(source(srcOffset + 2)) & 0xFF) << 6)
      destination(destOffset) = (outBuff >>> 16).asInstanceOf[Byte]
      destination(destOffset + 1) = (outBuff >>> 8).asInstanceOf[Byte]
      return 2
    }
    else {
      var outBuff: Int = ((DECODABET(source(srcOffset)) & 0xFF) << 18) | ((DECODABET(source(srcOffset + 1)) & 0xFF) << 12) | ((DECODABET(source(srcOffset + 2)) & 0xFF) << 6) | ((DECODABET(source(srcOffset + 3)) & 0xFF))
      destination(destOffset) = (outBuff >> 16).asInstanceOf[Byte]
      destination(destOffset + 1) = (outBuff >> 8).asInstanceOf[Byte]
      destination(destOffset + 2) = (outBuff).asInstanceOf[Byte]
      return 3
    }
  }

  /**
   * Decode.
   *
   * @param source
   *            the source
   *
   * @return the byte[]
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def decode(source: Array[Byte]): Array[Byte] = {
    var decoded: Array[Byte] = null
    decoded = decode(source, 0, source.length, Base64.NO_OPTIONS)
    return decoded
  }

  /**
   * Decode.
   *
   * @param source
   *            the source
   * @param off
   *            the off
   * @param len
   *            the len
   * @param options
   *            the options
   *
   * @return the byte[]
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def decode(source: Array[Byte], off: Int, len: Int, options: Int): Array[Byte] = {
    if (source == null) {
      throw new NullPointerException("Cannot decode null source array.")
    }
    if (off < 0 || off + len > source.length) {
      throw new IllegalArgumentException("Source array with length %d cannot have offset of %d and process %d bytes.".format(source.length, off, len))
    }
    if (len == 0) {
      return new Array[Byte](0)
    }
    else if (len < 4) {
      throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len)
    }
    var DECODABET: Array[Byte] = getDecodabet(options)
    var len34 = len * 3 / 4
    var outBuff: Array[Byte] = new Array[Byte](len34)
    var outBuffPosn: Int = 0
    var b4 = new Array[Byte](4)
    var b4Posn = 0
    var i = 0
    var sbiDecode: Byte = 0

      i = off

      breakable{
        while (i < off + len) {

          sbiDecode = DECODABET(source(i) & 0xFF)
          if (sbiDecode >= WHITE_SPACE_ENC) {
            if (sbiDecode >= EQUALS_SIGN_ENC) {
              b4(({
                b4Posn += 1; b4Posn
              })) = source(i)
              if (b4Posn > 3) {
                outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options)
                b4Posn = 0
                if (source(i) == EQUALS_SIGN) {
                  break
                }
              }
            }
          }
          else {
            throw new java.io.IOException("Bad Base64 input character decimal %d in array position %d".format( (source(i).asInstanceOf[Int]) & 0xFF, i))
          }

          i += 1
        }
      }

    val out = new Array[Byte](outBuffPosn)
    System.arraycopy(outBuff, 0, out, 0, outBuffPosn)
    return out
  }

  /**
   * Decode.
   *
   * @param s
   *            the s
   *
   * @return the byte[]
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def decode(s: String): Array[Byte] = decode(s, NO_OPTIONS)

  /**
   * Decode.
   *
   * @param s
   *            the s
   * @param options
   *            the options
   *
   * @return the byte[]
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def decode(s: String, options: Int): Array[Byte] = {
    if (s == null) {
      throw new NullPointerException("Input string was null.")
    }
    var bytes: Array[Byte] = null
    try {
      bytes = s.getBytes(PREFERRED_ENCODING)
    }
    catch {
      case uee: java.io.UnsupportedEncodingException => {
        bytes = s.getBytes
      }
    }
    bytes = decode(bytes, 0, bytes.length, options)
    var dontGunzip: Boolean = (options & DONT_GUNZIP) != 0
    if ((bytes != null) && (bytes.length >= 4) && (!dontGunzip)) {
      var head: Int = (bytes(0).asInstanceOf[Int] & 0xff) | ((bytes(1) << 8) & 0xff00)
      if (java.util.zip.GZIPInputStream.GZIP_MAGIC == head) {
        var bais: java.io.ByteArrayInputStream = null
        var gzis: java.util.zip.GZIPInputStream = null
        var baos: java.io.ByteArrayOutputStream = null
        var buffer: Array[Byte] = new Array[Byte](2048)
        var length: Int = 0
        try {
          baos = new java.io.ByteArrayOutputStream
          bais = new java.io.ByteArrayInputStream(bytes)
          gzis = new java.util.zip.GZIPInputStream(bais)
          while ((({
            length = gzis.read(buffer); length
          })) >= 0) {
            baos.write(buffer, 0, length)
          }
          bytes = baos.toByteArray
        }
        catch {
          case e: java.io.IOException => {
            e.printStackTrace
          }
        }
        finally {
          try {
            baos.close
          }
          catch {
            case e: Exception => {
            }
          }
          try {
            gzis.close
          }
          catch {
            case e: Exception => {
            }
          }
          try {
            bais.close
          }
          catch {
            case e: Exception => {
            }
          }
        }
      }
    }
    return bytes
  }

  /**
   * Decode to object.
   *
   * @param encodedObject
   *            the encoded object
   *
   * @return the object
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   * @throws ClassNotFoundException
   *             the class not found exception
   */
  def decodeToObject(encodedObject: String): AnyRef = {
    return decodeToObject(encodedObject, NO_OPTIONS, null)
  }

  /**
   * Decode to object.
   *
   * @param encodedObject
   *            the encoded object
   * @param options
   *            the options
   * @param loader
   *            the loader
   *
   * @return the object
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   * @throws ClassNotFoundException
   *             the class not found exception
   */
  def decodeToObject(encodedObject: String, options: Int, loader: ClassLoader): AnyRef = {
    var objBytes: Array[Byte] = decode(encodedObject, options)
    var bais: java.io.ByteArrayInputStream = null
    var ois: java.io.ObjectInputStream = null
    var obj: AnyRef = null
    try {
      bais = new java.io.ByteArrayInputStream(objBytes)
      if (loader == null) {
        ois = new java.io.ObjectInputStream(bais)
      }
      else {
        ois = new java.io.ObjectInputStream((bais)) {
          override def resolveClass(streamClass: java.io.ObjectStreamClass): Class[_] = {
            var c: Class[_] = Class.forName(streamClass.getName, false, loader)
            if (c == null) {
              return super.resolveClass(streamClass)
            }
            else {
              return c
            }
          }
        }
      }
      obj = ois.readObject
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
      case e: ClassNotFoundException => {
        throw e
      }
    }
    finally {
      try {
        bais.close
      }
      catch {
        case e: Exception => {
        }
      }
      try {
        ois.close
      }
      catch {
        case e: Exception => {
        }
      }
    }
    return obj
  }

  /**
   * Encode to file.
   *
   * @param dataToEncode
   *            the data to encode
   * @param filename
   *            the filename
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeToFile(dataToEncode: Array[Byte], filename: String): Unit = {
    if (dataToEncode == null) {
      throw new NullPointerException("Data to encode was null.")
    }
    var bos: Base64.OutputStream = null
    try {
      bos = new Base64.OutputStream(new java.io.FileOutputStream(filename), Base64.ENCODE)
      bos.write(dataToEncode)
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
    }
    finally {
      try {
        bos.close
      }
      catch {
        case e: Exception => {
        }
      }
    }
  }

  /**
   * Decode to file.
   *
   * @param dataToDecode
   *            the data to decode
   * @param filename
   *            the filename
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def decodeToFile(dataToDecode: String, filename: String): Unit = {
    var bos: Base64.OutputStream = null
    try {
      bos = new Base64.OutputStream(new java.io.FileOutputStream(filename), Base64.DECODE)
      bos.write(dataToDecode.getBytes(PREFERRED_ENCODING))
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
    }
    finally {
      try {
        bos.close
      }
      catch {
        case e: Exception => {
        }
      }
    }
  }

  /**
   * Decode from file.
   *
   * @param filename
   *            the filename
   *
   * @return the byte[]
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def decodeFromFile(filename: String): Array[Byte] = {
    var decodedData: Array[Byte] = null
    var bis: Base64.InputStream = null
    try {
      var file = new java.io.File(filename)
      var buffer: Array[Byte] = null
      var length = 0
      var numBytes = 0
      if (file.length > Integer.MAX_VALUE) {
        throw new java.io.IOException("File is too big for this convenience method (" + file.length + " bytes).")
      }
      buffer = new Array[Byte](file.length.asInstanceOf[Int])
      bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.DECODE)
      while ((({
        numBytes = bis.read(buffer, length, 4096); numBytes
      })) >= 0) {
        length += numBytes
      }
      decodedData = new Array[Byte](length)
      System.arraycopy(buffer, 0, decodedData, 0, length)
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
    }
    finally {
      try {
        bis.close
      }
      catch {
        case e: Exception => {
        }
      }
    }
    return decodedData
  }

  /**
   * Encode from file.
   *
   * @param filename
   *            the filename
   *
   * @return the string
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeFromFile(filename: String): String = {
    var encodedData: String = null
    var bis: Base64.InputStream = null
    try {
      var file = new java.io.File(filename)
      var buffer = new Array[Byte](Math.max((file.length * 1.4 + 1).asInstanceOf[Int], 40))
      var length = 0
      var numBytes = 0
      bis = new Base64.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base64.ENCODE)
      while ((({
        numBytes = bis.read(buffer, length, 4096); numBytes
      })) >= 0) {
        length += numBytes
      }
      encodedData = new String(buffer, 0, length, Base64.PREFERRED_ENCODING)
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
    }
    finally {
      try {
        bis.close
      }
      catch {
        case e: Exception => {
        }
      }
    }
    return encodedData
  }

  /**
   * Encode file to file.
   *
   * @param infile
   *            the infile
   * @param outfile
   *            the outfile
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def encodeFileToFile(infile: String, outfile: String): Unit = {
    var encoded: String = Base64.encodeFromFile(infile)
    var out: java.io.OutputStream = null
    try {
      out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outfile))
      out.write(encoded.getBytes("US-ASCII"))
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
    }
    finally {
      try {
        out.close
      }
      catch {
        case ex: Exception => {
        }
      }
    }
  }

  /**
   * Decode file to file.
   *
   * @param infile
   *            the infile
   * @param outfile
   *            the outfile
   *
   * @throws IOException
   *             Signals that an I/O exception has occurred.
   */
  def decodeFileToFile(infile: String, outfile: String): Unit = {
    var decoded: Array[Byte] = Base64.decodeFromFile(infile)
    var out: java.io.OutputStream = null
    try {
      out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outfile))
      out.write(decoded)
    }
    catch {
      case e: java.io.IOException => {
        throw e
      }
    }
    finally {
      try {
        out.close
      }
      catch {
        case ex: Exception => {
        }
      }
    }
  }

  /**The Constant NO_OPTIONS. */
  final val NO_OPTIONS = 0
  /**The Constant ENCODE. */
  final val ENCODE = 1
  /**The Constant DECODE. */
  final val DECODE = 0
  /**The Constant GZIP. */
  final val GZIP = 2
  /**The Constant DONT_GUNZIP. */
  final val DONT_GUNZIP = 4
  /**The Constant DO_BREAK_LINES. */
  final val DO_BREAK_LINES = 8
  /**The Constant URL_SAFE. */
  final val URL_SAFE = 16
  /**The Constant ORDERED. */
  final val ORDERED = 32
  /**The Constant MAX_LINE_LENGTH. */
  private final val MAX_LINE_LENGTH = 76
  /**The Constant EQUALS_SIGN. */
  private final val EQUALS_SIGN: Byte = '='.asInstanceOf[Byte]
  /**The Constant NEW_LINE. */
  private final val NEW_LINE: Byte = '\n'.asInstanceOf[Byte]
  /**The Constant PREFERRED_ENCODING. */
  private final val PREFERRED_ENCODING: String = "US-ASCII"
  /**The Constant WHITE_SPACE_ENC. */
  private final val WHITE_SPACE_ENC: Byte = -5
  /**The Constant EQUALS_SIGN_ENC. */
  private final val EQUALS_SIGN_ENC: Byte = -1
  /**The Constant _STANDARD_ALPHABET. */
  private final val _STANDARD_ALPHABET: Array[Byte] = Array('A'.asInstanceOf[Byte], 'B'.asInstanceOf[Byte], 'C'.asInstanceOf[Byte], 'D'.asInstanceOf[Byte], 'E'.asInstanceOf[Byte], 'F'.asInstanceOf[Byte], 'G'.asInstanceOf[Byte], 'H'.asInstanceOf[Byte], 'I'.asInstanceOf[Byte], 'J'.asInstanceOf[Byte], 'K'.asInstanceOf[Byte], 'L'.asInstanceOf[Byte], 'M'.asInstanceOf[Byte], 'N'.asInstanceOf[Byte], 'O'.asInstanceOf[Byte], 'P'.asInstanceOf[Byte], 'Q'.asInstanceOf[Byte], 'R'.asInstanceOf[Byte], 'S'.asInstanceOf[Byte], 'T'.asInstanceOf[Byte], 'U'.asInstanceOf[Byte], 'V'.asInstanceOf[Byte], 'W'.asInstanceOf[Byte], 'X'.asInstanceOf[Byte], 'Y'.asInstanceOf[Byte], 'Z'.asInstanceOf[Byte], 'a'.asInstanceOf[Byte], 'b'.asInstanceOf[Byte], 'c'.asInstanceOf[Byte], 'd'.asInstanceOf[Byte], 'e'.asInstanceOf[Byte], 'f'.asInstanceOf[Byte], 'g'.asInstanceOf[Byte], 'h'.asInstanceOf[Byte], 'i'.asInstanceOf[Byte], 'j'.asInstanceOf[Byte], 'k'.asInstanceOf[Byte], 'l'.asInstanceOf[Byte], 'm'.asInstanceOf[Byte], 'n'.asInstanceOf[Byte], 'o'.asInstanceOf[Byte], 'p'.asInstanceOf[Byte], 'q'.asInstanceOf[Byte], 'r'.asInstanceOf[Byte], 's'.asInstanceOf[Byte], 't'.asInstanceOf[Byte], 'u'.asInstanceOf[Byte], 'v'.asInstanceOf[Byte], 'w'.asInstanceOf[Byte], 'x'.asInstanceOf[Byte], 'y'.asInstanceOf[Byte], 'z'.asInstanceOf[Byte], '0'.asInstanceOf[Byte], '1'.asInstanceOf[Byte], '2'.asInstanceOf[Byte], '3'.asInstanceOf[Byte], '4'.asInstanceOf[Byte], '5'.asInstanceOf[Byte], '6'.asInstanceOf[Byte], '7'.asInstanceOf[Byte], '8'.asInstanceOf[Byte], '9'.asInstanceOf[Byte], '+'.asInstanceOf[Byte], '/'.asInstanceOf[Byte])
  /**The Constant _STANDARD_DECODABET. */
  private final val _STANDARD_DECODABET: Array[Byte] = Array(-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9)
  /**The Constant _URL_SAFE_ALPHABET. */
  private final val _URL_SAFE_ALPHABET: Array[Byte] = Array('A'.asInstanceOf[Byte], 'B'.asInstanceOf[Byte], 'C'.asInstanceOf[Byte], 'D'.asInstanceOf[Byte], 'E'.asInstanceOf[Byte], 'F'.asInstanceOf[Byte], 'G'.asInstanceOf[Byte], 'H'.asInstanceOf[Byte], 'I'.asInstanceOf[Byte], 'J'.asInstanceOf[Byte], 'K'.asInstanceOf[Byte], 'L'.asInstanceOf[Byte], 'M'.asInstanceOf[Byte], 'N'.asInstanceOf[Byte], 'O'.asInstanceOf[Byte], 'P'.asInstanceOf[Byte], 'Q'.asInstanceOf[Byte], 'R'.asInstanceOf[Byte], 'S'.asInstanceOf[Byte], 'T'.asInstanceOf[Byte], 'U'.asInstanceOf[Byte], 'V'.asInstanceOf[Byte], 'W'.asInstanceOf[Byte], 'X'.asInstanceOf[Byte], 'Y'.asInstanceOf[Byte], 'Z'.asInstanceOf[Byte], 'a'.asInstanceOf[Byte], 'b'.asInstanceOf[Byte], 'c'.asInstanceOf[Byte], 'd'.asInstanceOf[Byte], 'e'.asInstanceOf[Byte], 'f'.asInstanceOf[Byte], 'g'.asInstanceOf[Byte], 'h'.asInstanceOf[Byte], 'i'.asInstanceOf[Byte], 'j'.asInstanceOf[Byte], 'k'.asInstanceOf[Byte], 'l'.asInstanceOf[Byte], 'm'.asInstanceOf[Byte], 'n'.asInstanceOf[Byte], 'o'.asInstanceOf[Byte], 'p'.asInstanceOf[Byte], 'q'.asInstanceOf[Byte], 'r'.asInstanceOf[Byte], 's'.asInstanceOf[Byte], 't'.asInstanceOf[Byte], 'u'.asInstanceOf[Byte], 'v'.asInstanceOf[Byte], 'w'.asInstanceOf[Byte], 'x'.asInstanceOf[Byte], 'y'.asInstanceOf[Byte], 'z'.asInstanceOf[Byte], '0'.asInstanceOf[Byte], '1'.asInstanceOf[Byte], '2'.asInstanceOf[Byte], '3'.asInstanceOf[Byte], '4'.asInstanceOf[Byte], '5'.asInstanceOf[Byte], '6'.asInstanceOf[Byte], '7'.asInstanceOf[Byte], '8'.asInstanceOf[Byte], '9'.asInstanceOf[Byte], '-'.asInstanceOf[Byte], '_'.asInstanceOf[Byte])
  /**The Constant _URL_SAFE_DECODABET. */
  private final val _URL_SAFE_DECODABET: Array[Byte] = Array(-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9)
  /**The Constant _ORDERED_ALPHABET. */
  private final val _ORDERED_ALPHABET: Array[Byte] = Array('-'.asInstanceOf[Byte], '0'.asInstanceOf[Byte], '1'.asInstanceOf[Byte], '2'.asInstanceOf[Byte], '3'.asInstanceOf[Byte], '4'.asInstanceOf[Byte], '5'.asInstanceOf[Byte], '6'.asInstanceOf[Byte], '7'.asInstanceOf[Byte], '8'.asInstanceOf[Byte], '9'.asInstanceOf[Byte], 'A'.asInstanceOf[Byte], 'B'.asInstanceOf[Byte], 'C'.asInstanceOf[Byte], 'D'.asInstanceOf[Byte], 'E'.asInstanceOf[Byte], 'F'.asInstanceOf[Byte], 'G'.asInstanceOf[Byte], 'H'.asInstanceOf[Byte], 'I'.asInstanceOf[Byte], 'J'.asInstanceOf[Byte], 'K'.asInstanceOf[Byte], 'L'.asInstanceOf[Byte], 'M'.asInstanceOf[Byte], 'N'.asInstanceOf[Byte], 'O'.asInstanceOf[Byte], 'P'.asInstanceOf[Byte], 'Q'.asInstanceOf[Byte], 'R'.asInstanceOf[Byte], 'S'.asInstanceOf[Byte], 'T'.asInstanceOf[Byte], 'U'.asInstanceOf[Byte], 'V'.asInstanceOf[Byte], 'W'.asInstanceOf[Byte], 'X'.asInstanceOf[Byte], 'Y'.asInstanceOf[Byte], 'Z'.asInstanceOf[Byte], '_'.asInstanceOf[Byte], 'a'.asInstanceOf[Byte], 'b'.asInstanceOf[Byte], 'c'.asInstanceOf[Byte], 'd'.asInstanceOf[Byte], 'e'.asInstanceOf[Byte], 'f'.asInstanceOf[Byte], 'g'.asInstanceOf[Byte], 'h'.asInstanceOf[Byte], 'i'.asInstanceOf[Byte], 'j'.asInstanceOf[Byte], 'k'.asInstanceOf[Byte], 'l'.asInstanceOf[Byte], 'm'.asInstanceOf[Byte], 'n'.asInstanceOf[Byte], 'o'.asInstanceOf[Byte], 'p'.asInstanceOf[Byte], 'q'.asInstanceOf[Byte], 'r'.asInstanceOf[Byte], 's'.asInstanceOf[Byte], 't'.asInstanceOf[Byte], 'u'.asInstanceOf[Byte], 'v'.asInstanceOf[Byte], 'w'.asInstanceOf[Byte], 'x'.asInstanceOf[Byte], 'y'.asInstanceOf[Byte], 'z'.asInstanceOf[Byte])
  /**The Constant _ORDERED_DECODABET. */
  private final val _ORDERED_DECODABET: Array[Byte] = Array(-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9)

  /**
   * The Class InputStream.
   */
  class InputStream(in:java.io.InputStream,var options:Int) extends java.io.FilterInputStream(in) {

    locally{

      this.breakLines = (options & DO_BREAK_LINES) > 0
      this.encode = (options & ENCODE) > 0
      this.bufferLength = if (encode) 4 else 3
      this.buffer = new Array[Byte](bufferLength)
      this.position = -1
      this.lineLength = 0
      this.decodabet = getDecodabet(options)
    }


    /**
     * Instantiates a new input stream.
     *
     * @param in
     *            the in
     */
    def this(in: InputStream) {
      this(in, DECODE)
    }


    override def read: Int = {
      if (position < 0) {
        if (encode) {
          var b3 = new Array[Byte](3)
          var numBinaryBytes = 0

          var i = 0

          breakable{
            while (i < 3) {
              var b= in.read
              if (b >= 0) {
                b3(i) = b.asInstanceOf[Byte]
                numBinaryBytes += 1
              }
              else {
                break
              }
              i += 1
            }
          }

          if (numBinaryBytes > 0) {
            encode3to4(b3, 0, numBinaryBytes, buffer, 0, options)
            position = 0
            numSigBytes = 4
          }
          else {
            return -1
          }
        }
        else {
          var b4 = new Array[Byte](4)
          var i = 0

          breakable{
            while (i < 4) {
              {
                var b = 0
                do {
                  b = in.read
                } while (b >= 0 && decodabet(b & 0x7f) <= WHITE_SPACE_ENC)
                if (b < 0) {
                  break
                }
                b4(i) = b.asInstanceOf[Byte]
              }
              i += 1
            }
          }

          if (i == 4) {
            numSigBytes = decode4to3(b4, 0, buffer, 0, options)
            position = 0
          }
          else if (i == 0) {
            return -1
          }
          else {
            throw new java.io.IOException("Improperly padded Base64 input.")
          }
        }
      }
      if (position >= 0) {
        if (position >= numSigBytes) {
          return -1
        }
        if (encode && breakLines && lineLength >= MAX_LINE_LENGTH) {
          lineLength = 0
          return '\n'
        }
        else {
          ({
            lineLength += 1; lineLength
          })
          var b: Int = buffer(({
            position += 1; position
          }))
          if (position >= bufferLength) {
            position = -1
          }
          return b & 0xFF
        }
      }
      else {
        throw new java.io.IOException("Error in Base64 code reading stream.")
      }
    }

    override def read(dest: Array[Byte], off: Int, len: Int): Int = {
      var i = 0
      var b = 0


      breakable{
        while (i < len) {
          b = read
          if (b >= 0) {
            dest(off + i) = b.asInstanceOf[Byte]
          }
          else if (i == 0) {
            return -1
          }
          else {
            break
          }
          i += 1
        }
      }

      return i
    }

    /**The encode. */
    private var encode: Boolean = false
    /**The position. */
    private var position: Int = 0
    /**The buffer. */
    private var buffer: Array[Byte] = null
    /**The buffer length. */
    private var bufferLength = 0
    /**The num sig bytes. */
    private var numSigBytes = 0
    /**The line length. */
    private var lineLength = 0
    /**The break lines. */
    private var breakLines = false
    /**The decodabet. */
    private var decodabet: Array[Byte] = null
  }

  /**
   * The Class OutputStream.
   */
  class OutputStream(o: java.io.OutputStream,var options: Int)  extends java.io.FilterOutputStream(o) {
    /**
     * Instantiates a new output stream.
     *
     * @param out
     *            the out
     */
    def this(o: java.io.OutputStream) {
      this(o, ENCODE)
    }

    /**
     * Instantiates a new output stream.
     *
     * @param out
     *            the out
     * @param options
     *            the options
     */
    locally{
      this.breakLines = (options & DO_BREAK_LINES) != 0
      this.encode = (options & ENCODE) != 0
      this.bufferLength = if (encode) 3 else 4
      this.buffer = new Array[Byte](bufferLength)
      this.position = 0
      this.lineLength = 0
      this._suspendEncoding = false
      this.b4 = new Array[Byte](4)
      this.decodabet = getDecodabet(options)
    }

    override def write(theByte: Int): Unit = {
      if (_suspendEncoding) {
        this.out.write(theByte)
        return
      }
      if (encode) {
        buffer(({
          position += 1; position
        })) = theByte.asInstanceOf[Byte]
        if (position >= bufferLength) {
          this.out.write(encode3to4(b4, buffer, bufferLength, options))
          lineLength += 4
          if (breakLines && lineLength >= MAX_LINE_LENGTH) {
            this.out.write(NEW_LINE)
            lineLength = 0
          }
          position = 0
        }
      }
      else {
        if (decodabet(theByte & 0x7f) > WHITE_SPACE_ENC) {
          buffer(({
            position += 1; position
          })) = theByte.asInstanceOf[Byte]
          if (position >= bufferLength) {
            var len: Int = Base64.decode4to3(buffer, 0, b4, 0, options)
            out.write(b4, 0, len)
            position = 0
          }
        }
        else if (decodabet(theByte & 0x7f) != WHITE_SPACE_ENC) {
          throw new java.io.IOException("Invalid character in Base64 data.")
        }
      }
    }

    override def write(theBytes: Array[Byte], off: Int, len: Int): Unit = {
      if (_suspendEncoding) {
        this.out.write(theBytes, off, len)
        return
      }
      {
        var i = 0
        while (i < len) {
          write(theBytes(off + i))
          i += 1
        }
      }
    }

    /**
     * Flush base64.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    def flushBase64(){
      if (position > 0) {
        if (encode) {
          out.write(encode3to4(b4, buffer, position, options))
          position = 0
        }
        else {
          throw new java.io.IOException("Base64 input not properly padded.")
        }
      }
    }

    override def close: Unit = {
      flushBase64
      super.close
      buffer = null
      out = null
    }

    /**
     * Suspend encoding.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    def suspendEncoding: Unit = {
      flushBase64
      this._suspendEncoding = true
    }

    /**
     * Resume encoding.
     */
    def resumeEncoding(): Unit = {
      this._suspendEncoding = false
    }

    /**The encode. */
    private var encode = false
    /**The position. */
    private var position = 0
    /**The buffer. */
    private var buffer: Array[Byte] = null
    /**The buffer length. */
    private var bufferLength = 0
    /**The line length. */
    private var lineLength = 0
    /**The break lines. */
    private var breakLines: Boolean = false
    /**The b4. */
    private var b4: Array[Byte] = null
    /**The suspend encoding. */
    private var _suspendEncoding = false
    /**The decodabet. */
    private var decodabet: Array[Byte] = null
  }

}

/**
 * Instantiates a new base64.
 */
class Base64 private{}