package com.etagi.crm

import java.io.{InputStream, DataInputStream}

object Packet {
  val headerLength = 20

  def getStream(stream: InputStream) : Stream[Packet] = {
    val packet = readPacket(stream)

    packet match {
      case Some(packet) => packet #:: getStream(stream)
      case None => Stream.empty
    }
  }

  def readPacket(stream: InputStream) : Option[Packet] = {
    if (stream.available == -1)
        return None

    val dataInputStream = new DataInputStream(stream)

    try {
      val header = new PacketHeader(
        dataInputStream.readByte,
        dataInputStream.readByte,
        dataInputStream.readInt,
        dataInputStream.readShort,
        dataInputStream.readInt,
        dataInputStream.readLong
      )


      if (header.bodyLength < 0 || header.bodyLength >= 2000)
        println(header)

      var body = new Array[Byte](header.bodyLength)
      dataInputStream.read(body)

      return new Some(new Packet(header, body))
    } catch {
      case e: java.io.EOFException =>
      return None
    }
  }
}

class Packet(header: PacketHeader, body: Array[Byte]) {
  override def toString(): String = "Packet v" + header.version + " length: " + header.bodyLength;
}

case class PacketHeader (
  kind: Byte,
  version: Short,
  bodyLength: Int,
  status: Short,
  action: Int,
  jobId: Long
)
