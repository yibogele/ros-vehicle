package com.venusiot.vehicle.message;

import com.core.venusiot.common.dto.DownVenusMessage;
import com.core.venusiot.common.dto.VenusMessage;
import com.core.venusiot.common.utils.Base64Decoder;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * avro序列化/反序列化
 *
 * @author commuli
 * @date 2018/6/19
 */
public class AvroMessageSerializer {

    public static byte[] serialize(VenusMessage instance) throws Exception {
        DatumWriter<VenusMessage> datumWriter = new SpecificDatumWriter<>(VenusMessage.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        datumWriter.write(instance, binaryEncoder);
        return outputStream.toByteArray();
    }


//    public static VenusMessage deserialize(byte[] bytes) throws Exception {
//        DatumReader<VenusMessage> userDatumReader = new SpecificDatumReader<>(VenusMessage.class);
//        BinaryDecoder binaryEncoder = DecoderFactory.get().directBinaryDecoder(new ByteArrayInputStream(bytes), null);
//        return userDatumReader.read(new VenusMessage(), binaryEncoder);
//    }

    public static DownVenusMessage deserialize(byte[] bytes) throws Exception {

        DatumReader<DownVenusMessage> userDatumReader = new SpecificDatumReader<>(DownVenusMessage.class);
        BinaryDecoder binaryEncoder = DecoderFactory.get().directBinaryDecoder(new ByteArrayInputStream(bytes), null);
        return userDatumReader.read(new DownVenusMessage(), binaryEncoder);
    }

    public static <T> T jsonDecodeToAvro(String inputString, Class<T> className, Schema schema) {
        T returnObject = null;
        try {
            JsonDecoder jsonDecoder = DecoderFactory.get().jsonDecoder(schema, inputString);
            SpecificDatumReader<T> reader = new SpecificDatumReader<T>(className);
            returnObject = reader.read(null, jsonDecoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObject;
    }

    public static VenusMessage mapToVenusMessage(Map<String, Object> map) throws Exception {
        VenusMessage venusMessage = new VenusMessage();
        venusMessage.setDeviceId(map.get("deviceId") != null ? map.get("deviceId").toString() : "");
        venusMessage.setDeviceType(map.get("deviceType") != null ? map.get("deviceType").toString() : "");
        venusMessage.setFeedId(map.get("feedId") != null ? map.get("feedId").toString() : "");
        venusMessage.setApiKey(map.get("apiKey") != null ? map.get("apiKey").toString() : "");
        venusMessage.setProductKey(map.get("productKey") != null ? map.get("productKey").toString() : "");
        venusMessage.setDataType(map.get("dataType") != null ? map.get("dataType").toString() : "");
        venusMessage.setProtocolType(map.get("protocolType") != null ? map.get("protocolType").toString() : "");
        venusMessage.setDateTime(map.get("dateTime") != null ? map.get("dateTime").toString() : "");
        venusMessage.setCurMapType(map.get("curMapType") != null ? map.get("curMapType").toString() : "");
        venusMessage.setLongitude(map.get("longitude") != null ? map.get("longitude").toString() : "");
        venusMessage.setLatitude(map.get("latitude") != null ? map.get("latitude").toString() : "");
        venusMessage.setDirection(map.get("direction") != null ? map.get("direction").toString() : "");
        //Object body = map.get("body");
        //String s = body.toString();
        //System.out.println(s);
        //String decode = Base64Decoder.decode(s);
        //System.out.println(decode);
        venusMessage.setBody(map.get("body") != null ? new String(Base64Decoder.decodeToBytes(map.get("body").toString()), "utf-8") : "");
        venusMessage.setAttachments(map.get("attachments") != null ? map.get("attachments").toString() : "");
        venusMessage.setMessageType(map.get("messageType") != null ? map.get("messageType").toString() : "");
        venusMessage.setAlarm(map.get("alarm") != null ? map.get("alarm").toString() : "");
        venusMessage.setSn(map.get("sn") != null ? map.get("sn").toString() : "");

        return venusMessage;
    }

}
