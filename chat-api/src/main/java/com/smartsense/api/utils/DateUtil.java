/*
 * Copyright 2024 smartSense Consulting Solutions Pvt. Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartsense.api.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * Contain Date related custom function
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@UtilityClass
public class DateUtil {

    private static final DateTimeFormatter ISO8601 = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendPattern("[['-']MM[['-']dd[['T']HH[[':']mm[[':']ss['.'SSS]]]]]]")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter();


    /**
     * Convert dates given in a basic ISO 8601 date
     *
     * @param dateStr Date in String format
     * @return Date object based on provided string Date
     */
    public static Date convertToDate(String dateStr) {
        LocalDateTime l = LocalDateTime.parse(dateStr, ISO8601);
        return Date.from(l.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert java date to ISO datetime stamp format
     *
     * @param date Java Date Object
     * @return Date in specified format
     */
    public static String formatDate(Date date) {
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return targetFormat.format(date);
    }

    /**
     * Check if provided date has valid ISO datetime stamp format
     *
     * @param dateString date in String format
     * @return boolean flag whether provided date is a valid ISO8601 date
     */
    public static boolean isValidDate(String dateString) {
        try {
            LocalDateTime.parse(dateString, ISO8601);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
