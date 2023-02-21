package com.bingle.ameba.bingle_bar.common_functions.firebase_chat_managers;

/**
 * Created by Master Gaurav Singla on 13/5/18.
 */

public class FirebaseDatabaseSchemaManager {

    public class DATABASE_STORE_LIVE {

        public final static String USERS_STORE = "Users-Live";

        public final static String CHAT_CHANNELS = "ChatChannels-Live";
    }

    public class DATABASE_STORE_DEBUG {

        public final static String USERS_STORE = "Users-Debug";

        public final static String CHAT_CHANNELS = "ChatChannels-Debug";
    }

    public class FIELDS {

        public class USER_FIELDS {

            public final static String FULL_NAME = "FullName";
            public final static String USER_ID = "UserId";
            public final static String PRESENCE_STATUS = "PresenceStatus";
            public final static String USER_PROFILE_PIC = "UserProfilePic";
            public final static String CHAT_USERS = "ChatUsers";
            public final static String RESTAURANT_ID = "RestaurantId";
            public final static String USER_TOKEN = "FcmToken";
            public final static String BLOCK_STATUS = "BlockedStatus";
            public final static String BLOCK_BY = "BlockedBy";

            public final static String ChatUsers = "ChatUsers";


        }


        public class CHAT_CHANNEL_FIELDS {

            public final static String CHANNEL_ID = "ChannelId";

            public final static String MESSAGE_ID = "MessageId";
            public final static String SENDER_ID = "SenderId";
            public final static String MESSAGE = "Message";
            public final static String MESSAGE_TIME = "MessageTime";
        }
    }
}

