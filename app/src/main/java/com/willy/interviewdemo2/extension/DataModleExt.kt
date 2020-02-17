package com.willy.interviewdemo2.extension

import com.willy.interviewdemo2.data.api.model.Drama
import com.willy.interviewdemo2.data.db.entities.DramaEntity
import java.util.*

// === Data ===
/**
 * 換換 API、DB 格式的 Drama Data
 */
fun Drama.toEntity(): DramaEntity =
    DramaEntity(dramaId, name, totalViews, Date().from_yyyyMMddHHmmss(createdAt), thumb, rating)

fun DramaEntity.toDrama(): Drama =
    Drama(dramaId, name, totalViews, createdAt.yyyyMMddTHHmmss, thumb, rating)