package com.etagi.crm

import java.net.Socket
import java.util.Calendar

class TcpConnectionHandler(socket: Socket) extends Runnable {
  val inputStream = socket.getInputStream

  def run() {
    val startTime = System.nanoTime
    Packet.getStream(inputStream) foreach PacketDispatcher.handle
    println(s"done reading packets in ${ (System.nanoTime - startTime) / 10e9 } seconds ")
  }
}
