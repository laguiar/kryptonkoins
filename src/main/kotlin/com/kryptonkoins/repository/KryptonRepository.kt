package com.kryptonkoins.repository

import com.kryptonkoins.domain.Krypton
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface KryptonRepository : ReactiveMongoRepository<Krypton, UUID>