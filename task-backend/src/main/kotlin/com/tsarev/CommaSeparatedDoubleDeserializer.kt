package com.tsarev

import com.fasterxml.jackson.core.Base64Variant
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers

/**
 * Deserializer for primitive double values, replacing "," with ".".
 *
 * **ImplNote**: This implementation is heavily dependent on [NumberDeserializers.DoubleDeserializer]
 * so use it with caution.
 */
class CommaSeparatedPrimitiveDoubleDeserializer : NumberDeserializers.DoubleDeserializer(
    java.lang.Double.TYPE, 0.0 // See [NumberDeserializers.DoubleDeserializer] implementation.
) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Double {
        val parserProxy = object : JsonParser() {
            // The magic goes here.
            override fun getText() = p.text.replace(",", ".")
            // Just passing through.
            override fun close() = p.close()
            override fun version() = p.version()
            override fun getCodec() = p.codec
            override fun setCodec(c: ObjectCodec) {
                p.codec = c
            }
            override fun isClosed() = p.isClosed
            override fun getParsingContext() = p.parsingContext
            override fun getTokenLocation() = p.tokenLocation
            override fun getCurrentLocation() = p.currentLocation
            override fun nextToken() = p.nextToken()
            override fun nextValue() = p.nextValue()
            override fun skipChildren() = p.skipChildren()
            override fun getCurrentToken() = p.currentToken
            override fun getCurrentTokenId() = p.currentTokenId
            override fun hasCurrentToken() = p.hasCurrentToken()
            override fun hasTokenId(id: Int) = p.hasTokenId(id)
            override fun hasToken(t: JsonToken?) = p.hasToken(t)
            override fun clearCurrentToken() = p.clearCurrentToken()
            override fun getLastClearedToken() = p.lastClearedToken
            override fun overrideCurrentName(name: String?) = p.overrideCurrentName(name)
            override fun getCurrentName() = p.currentName
            override fun getTextCharacters() = p.textCharacters
            override fun getTextLength() = p.textLength
            override fun getTextOffset() = p.textOffset
            override fun hasTextCharacters() = p.hasTextCharacters()
            override fun getNumberValue() = p.numberValue
            override fun getNumberType() = p.numberType
            override fun getIntValue() = p.intValue
            override fun getLongValue() = p.longValue
            override fun getBigIntegerValue() = p.bigIntegerValue
            override fun getFloatValue() = p.floatValue
            override fun getDoubleValue() = p.doubleValue
            override fun getDecimalValue() = p.decimalValue
            override fun getBinaryValue(bv: Base64Variant?) = p.binaryValue
            override fun getValueAsString(def: String?) = p.valueAsString
        }
        return super.deserialize(parserProxy, ctxt)
    }
}