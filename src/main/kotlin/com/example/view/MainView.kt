package com.example.view

import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import tornadofx.*
import javax.swing.GroupLayout

import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainView : View("Desktop App for RasPi") {

    private val service: ExecutorService = Executors.newFixedThreadPool(5)
    override val root: AnchorPane by fxml("/fxml/main_view.fxml")
    private val setButton: Button by fxid<Button>("setButton")
    private val digitalClock: HBox by fxid<HBox>("Digital_Clock")
    private val analogClock: GroupLayout.Group by fxid<GroupLayout.Group>("Analog_Clock")

    init {
        val digitalClockThread : Thread = Thread(Runnable{runDigitalClock()})
        service.execute(digitalClockThread)
        //digitalClockThread.start()
        setButton.action {
            //digitalClockThread.stop()
            //digitalClockThread.start()
            service.shutdownNow()
            service.execute(digitalClockThread)
        }
    }

    private fun runDigitalClock(){
        val hourDigit : Text = digitalClock.children[0] as Text
        val minuteDigit : Text = digitalClock.children[2] as Text
        val current : Triple<Int, Int, Int> = getTime()
        var second = current.third
        var hour = current.first
        var minute = current.second
        try {
            do {
                do {
                    if (hour < 10) {
                        hourDigit.text = "0$hour"
                    } else {
                        hourDigit.text = hour.toString()
                    }
                    do {
                        if (minute < 10) {
                            minuteDigit.text = "0$minute"
                        } else {
                            minuteDigit.text = minute.toString()
                        }
                        do {
                            Thread.sleep(1000)
                            second++
                        } while (second < 60)
                        second = 0
                        minute++
                    } while (minute < 60)
                    minute = 0
                    hour++
                } while (hour < 24)
                hour = 0
            } while (true)
        }catch(e : Exception){
            throw RuntimeException(e);
        }
    }

    private fun getTime() : Triple<Int, Int, Int> {
        val datetime = LocalDateTime.now()
        return Triple(datetime.hour, datetime.minute, datetime.second)
    }
}
