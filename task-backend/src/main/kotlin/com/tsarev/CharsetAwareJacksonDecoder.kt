package com.tsarev

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import feign.Response
import feign.codec.Decoder
import java.io.BufferedReader
import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Effective clone of [feign.jackson.JacksonDecoder], but with
 * use of custom [Charset].
 */
class CharsetAwareJacksonDecoder(
    private val mapper: ObjectMapper,
    private val encoding: Charset
) : Decoder {

    @Throws(IOException::class)
    override fun decode(response: Response, type: Type?): Any? {
        if (response.body() == null) return null
        var reader = response.body().asReader(encoding)
        if (!reader.markSupported()) {
            reader = BufferedReader(reader, 1)
        }
        return try {
            // Read the first byte to see if we have any data
            reader.mark(1)
            if (reader.read() == -1) {
                return null // Eagerly returning null avoids "No content to map due to end-of-input"
            }
            reader.reset()
            mapper.readValue<Any>(reader, mapper.constructType(type))
        } catch (e: RuntimeJsonMappingException) {
            if (e.cause != null && e.cause is IOException) {
                throw IOException::class.java.cast(e.cause)
            }
            throw e
        }
    }
}