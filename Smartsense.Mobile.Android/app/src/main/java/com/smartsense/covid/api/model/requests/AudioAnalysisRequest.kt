package com.smartsense.covid.api.model.requests

class AudioToAnalysisRequest : BaseApiRequest() {
    lateinit var audio: ByteArray
}