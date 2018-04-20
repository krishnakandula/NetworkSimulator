package com.krishnakandula.network.transport;

abstract class TransportMsg {
    char msgType;
    byte sourceId;
    byte destinationId;
    short sequenceNum;
}
