package com.etagi.crm

import java.net.Socket

class TcpConnectionHandler(socket: Socket) extends Runnable {
  val inputStream = socket.getInputStream

  def run() {
    Packet.getStream(inputStream) foreach PacketDispatcher.handle
    println("done reading packets")
  }
}
