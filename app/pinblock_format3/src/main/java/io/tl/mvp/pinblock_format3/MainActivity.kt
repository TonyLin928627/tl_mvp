package io.tl.mvp.pinblock_format3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.experimental.xor


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)

        val pin  = "abcdef"
        val pan = "1111222233334444"

        val pinBlock = generatePinBlockFormat3(Pair(pin, pan), this::generateFormat3PinField, this::generateFormat3PanField)

        Log.d("ScreenMainActivity", pinBlock)
    }

    private fun generateFormat3PinField(pin: String): String{

        // PIN field，64bit，16 hex numbers
        // 0x3 + PIN length + PIN (fill with random hex numbers if the pin length is less than 14)
        var pinField:String = "3" + Integer.toHexString(pin.length) + pin

        // Pick hex numbers in 0xA~0xF
        Random().let { random ->
            for (i in 0 until 14 - pin.length) {
                pinField += Integer.toHexString(random.nextInt(6) + 10)
            }
        }

        return pinField
    }

    private fun generateFormat3PanField(pan: String): String {

        // PAN域，64bit，16位十六进制数字
        // 固定值0x0000 + PAN，去掉校验数字，从右边数12位，不足12位左补0x0
        val panWithoutCheckDigit = pan.substring(0, pan.length - 1)

        return ("0000" + if (panWithoutCheckDigit.length > 12) panWithoutCheckDigit
            .substring(
                panWithoutCheckDigit.length - 12,
                panWithoutCheckDigit.length
            ) else String.format(
            "%12s", panWithoutCheckDigit
        ).replace(' ', '0'))
    }

    private fun generatePinBlockFormat3(pinAndPan: Pair<String, String>,
                                        pinFieldGenerator: (String)->String,
                                        panFieldGenerator: (String)->String,): String {

        val pinField = pinFieldGenerator(pinAndPan.first)
        val panField = panFieldGenerator(pinAndPan.second)

        //Convert to byte array
        val pinFieldByteArray: ByteArray = hexStringToByteArray(pinField)
        val panFieldByteArray: ByteArray = hexStringToByteArray(panField)
        // do xor
        val pinBlockByteArray = ByteArray(8)
        pinBlockByteArray.forEachIndexed { i, _ ->
            pinBlockByteArray[i] = (pinFieldByteArray[i] xor panFieldByteArray[i])
        }

        // Convert to hex string
        return byteArrayToString(pinBlockByteArray).toUpperCase(Locale.ROOT)
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

    private fun byteArrayToString(bytes: ByteArray): String {
        val hex =  StringBuilder(bytes.size * 2);

        bytes.forEach { b->
            hex.append(String.format("{%02X},", b))
        }

        return hex.toString()
    }
}