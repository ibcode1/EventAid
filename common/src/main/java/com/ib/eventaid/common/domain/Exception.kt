package com.ib.eventaid.common.domain

import java.io.IOException


class NoMoreEventsException(message: String): Exception(message)
class NoMorePerformersException(message: String): Exception(message)

class NetworkUnavailableException(message: String = "No network available :(") : IOException(message)

class NetworkException(message: String): Exception(message)
