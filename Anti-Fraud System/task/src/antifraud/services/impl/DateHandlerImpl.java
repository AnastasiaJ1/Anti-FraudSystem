package antifraud.services.impl;

import antifraud.services.DateHandler;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class DateHandlerImpl implements DateHandler {
    @Override
    public Date checkDate(String date) {
        if (date == null) return null;
        try {
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return s.parse(date);
        } catch (Exception e){
            return null;
        }
    }
}
