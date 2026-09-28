package concrete.goonies.bulkbusketbackend.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/27 08:50
 * Author: Scarra Luba
 */

public class SmsUtil {

    public static  final String  FROM_NUMBER ="+17372508034";
    public static  final String  SID_KEY ="AC2370b075abc6c825be02cc7de55b6107";
    public static  final String  TOKEN_KEY ="bf56fa0131342d1548ea76e6b743e219";

    public static  void  sendSms(String to, String text)
    {
        Twilio.init(SID_KEY,TOKEN_KEY);
        Message message = creator(new PhoneNumber(to),new PhoneNumber(FROM_NUMBER),text).create();
    }
}
