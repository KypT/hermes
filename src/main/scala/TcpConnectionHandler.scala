package com.etagi.crm

import java.net.Socket
import java.util.Calendar

class TcpConnectionHandler(socket: Socket) extends Runnable {
  val inputStream = socket.getInputStream

  def run() {
    val startTime = System.currentTimeMillis
    Packet.getStream(inputStream) foreach PacketDispatcher.handle
    println(s"done reading packets in ${ (System.currentTimeMillis - startTime) / 1000.0 } seconds ")
  }
}
