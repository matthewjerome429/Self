package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.BookingStatusKey;

import java.util.List;

/**
 * Created by shane.tian.xia on 12/14/2017.
 */
public interface BookingStatusDAO  extends CrudRepository<BookingStatus, BookingStatusKey> {

    public BookingStatus findOne(BookingStatusKey key);

    @Query(nativeQuery = true, value="SELECT status_code FROM tb_booking_status WHERE app_code=:appCode AND action IN :actions")
    public List<String> findStatusCodeByAppCodeAndActionIn(@Param("appCode") String appCode, @Param("actions") List<String> actions );
    
    
    @Query(nativeQuery = true, value="SELECT * FROM tb_booking_status WHERE app_code=:appCode AND action IN ('ENABLED','DISABLED')")
    public List<BookingStatus> findAvailableStatus(@Param("appCode") String appCode);
}
