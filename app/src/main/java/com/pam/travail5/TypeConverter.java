//package com.pam.travail5;
//
//        import com.pam.travail5.message.Message;
//
//public class TypeConverter {
//
//    @androidx.room.TypeConverter
//    public static Message.TYPE fromInt(int type) {
//        if (type == Message.TYPE.Incoming.value) return Message.TYPE.Incoming;
//        if (type == Message.TYPE.Outgoing.value) return Message.TYPE.Outgoing;
//        return null;
//    }
//
//    @androidx.room.TypeConverter
//    public static int fromType(Message.TYPE type) {
//        return type.value;
//    }
//}
