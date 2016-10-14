package me.shakiba.jdbi.annotation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.persistence.TemporalType;

abstract class AnnoType {

    public static AnnoType String = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return String.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return rs.getString(name);
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return false;
        }
    };
    public static AnnoType Long = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Long.class.isAssignableFrom(clazz)
                    || long.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return rs.getLong(name);
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return false;
        }
    };
    public static AnnoType Int = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Integer.class.isAssignableFrom(clazz)
                    || int.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return rs.getInt(name);
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return false;
        }
    };
    public static AnnoType Double = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Double.class.isAssignableFrom(clazz)
                    || double.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return rs.getDouble(name);
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return false;
        }
    };
    public static AnnoType Float = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Float.class.isAssignableFrom(clazz)
                    || float.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return rs.getFloat(name);
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return false;
        }
    };
    public static AnnoType Boolean = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Boolean.class.isAssignableFrom(clazz)
                    || boolean.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return rs.getBoolean(name);
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return false;
        }
    };
    public static AnnoType Date = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Date.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return rs.getDate(name);
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return temper == null || TemporalType.DATE.equals(temper);
        }
    };
    public static AnnoType Time = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Date.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return new Date(rs.getTime(name).getTime());
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return TemporalType.TIME.equals(temper);
        }
    };
    public static AnnoType TimeStamp = new AnnoType() {
        @Override
        public boolean isAssignableFrom(Class<?> clazz) {
            return Date.class.isAssignableFrom(clazz);
        }

        @Override
        public Object getValue(ResultSet rs, String name) throws SQLException {
            return new Date(rs.getTimestamp(name).getTime());
        }

        @Override
        public boolean isDateObject(TemporalType temper) {
            return TemporalType.TIMESTAMP.equals(temper);
        }
    };
    public static AnnoType[] primitives = {String, Long, Int, Double, Float,
        Boolean, Date};

    public static AnnoType[] dates = {Date, Time, TimeStamp};

    public static AnnoType of(Class<?> clazz) throws IllegalArgumentException {
        for (AnnoType annoType : AnnoType.primitives) {
            if (annoType.isAssignableFrom(clazz)) {
                return annoType;
            }
        }
        throw new IllegalArgumentException();
    }

    public static AnnoType of(Class<?> clazz, TemporalType temper) throws IllegalArgumentException {
        if (Date.class.isAssignableFrom(clazz) && temper != null) {
            for (AnnoType annoType : dates) {
                if (annoType.isDateObject(temper)) {
                    return annoType;
                }
            }
            throw new IllegalArgumentException();
        } else {
            return of(clazz);
        }
    }

    public abstract boolean isAssignableFrom(Class<?> type);

    public abstract Object getValue(ResultSet rs, String name)
            throws SQLException;

    public abstract boolean isDateObject(TemporalType temper);
}
