package io.tl.mvp.pinblock.screens.compute.mvp

import io.reactivex.Completable
import io.reactivex.Single
import io.tl.mvp.lib.MvpModel
import io.tl.mvp.pinblock.application.PinBlockDataBridge
import java.util.*
import kotlin.experimental.xor

class ComputeModel(dataBridge: PinBlockDataBridge): MvpModel<PinBlockDataBridge>(dataBridge) {
    private var pin: String? = null

    fun getPan(): Single<String> = Single.just(dataBridge.HardCodedPan)

    fun getPin(): Single<String>  = Single.just(pin ?: "")

    fun setPin(pin: String): Completable {
        this.pin = pin
        return Completable.complete()
    }

    private fun generateFormat3PinField(pin: String): String{

        // PIN field，64bit，16 hex numbers
        var pinField:String = "3" + Integer.toHexString(pin.length) + pin

        // Randomly pick hex numbers in 0xA~0xF
        Random().let { random ->
            for (i in 0 until 14 - pin.length) {
                pinField += Integer.toHexString(random.nextInt(6) + 10)
            }
        }

        return pinField
    }

    private fun generateFormat3PanField(pan: String): String {

        // PAN field，64bit，16 hex numbers
        val panWithoutCheckDigit = pan.substring(0, pan.length - 1)

        return ("0000" + if (panWithoutCheckDigit.length > 12) panWithoutCheckDigit
            .substring(
                panWithoutCheckDigit.length - 12,
                panWithoutCheckDigit.length
            ) else String.format(
            "%12s", panWithoutCheckDigit
        ).replace(' ', '0'))
    }

    fun generatePinBlockFormat3(pinFieldGenerator: (String)->String = this::generateFormat3PinField,
                                 panFieldGenerator: (String)->String = this::generateFormat3PanField): Single<PinBlockComputation> {

         return Single.create { emitter->
             val pinField = pinFieldGenerator(getPin().blockingGet())
             val panField = panFieldGenerator(getPan().blockingGet())

             //Convert to byte array
             val pinFieldByteArray: ByteArray = hexStringToByteArray(pinField)
             val panFieldByteArray: ByteArray = hexStringToByteArray(panField)

             // do xor
             val pinBlockByteArray = ByteArray(8)
             pinBlockByteArray.forEachIndexed { i, _ ->
                 pinBlockByteArray[i] = (pinFieldByteArray[i] xor panFieldByteArray[i])
             }

             emitter.onSuccess(
                 PinBlockComputation(
                     first = pinFieldByteArray,
                     second = panFieldByteArray,
                     third = pinBlockByteArray
                 )
             )
         }
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4)
                    + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}

typealias PinBlockComputation = Triple<ByteArray, ByteArray, ByteArray>