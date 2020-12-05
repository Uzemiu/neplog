package cn.neptu.neplog.config.sql;

import org.hibernate.dialect.MySQL8Dialect;

public class MySqlDialect extends MySQL8Dialect {

    @Override
    public String getTableTypeString(){
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";
    }

}
