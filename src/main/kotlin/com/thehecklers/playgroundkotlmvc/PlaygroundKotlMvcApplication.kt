package com.thehecklers.playgroundkotlmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import javax.annotation.PostConstruct
import kotlin.random.Random

@SpringBootApplication
class PlaygroundKotlMvcApplication

fun main(args: Array<String>) {
	runApplication<PlaygroundKotlMvcApplication>(*args)
}

@Component
class DataLoader(private val repo: ShipRepository) {

	@PostConstruct
	fun load() {
		val shipNames =
			listOf("Ch'Tang", "Gr'oth", "Hegh'ta", "M'Char", "Maht-H'a", "Ning'tao", "Pagh", "T'Ong", "Vor'nak", "Ya'Vang")
		val captains =
			listOf("Martok", "Koloth", "Kurn", "Kaybok", "Nu'Daq", "Lurkan", "Kargan", "K'Temoc", "Tanas")

		val rnd = Random

		for (x in 0..999) {
			repo.save(Ship(name = shipNames.get(rnd.nextInt(shipNames.size)), captain = captains.get(rnd.nextInt(captains.size))))
		}

		repo.findAll().forEach { println(it) }
	}
}

@RestController
@RequestMapping("/ships")
class ShipController(private val repo: ShipRepository) {
	@GetMapping
	fun getAllShips() = repo.findAll()

	@GetMapping("/{id}")
	fun getShipById(@PathVariable id: String) = repo.findById(id)

	@GetMapping("/search")
	fun getShipByCaptain(@RequestParam(defaultValue = "Martok") captain: String) = repo.findShipByCaptain(captain)
}

interface ShipRepository : CrudRepository<Ship, String> {
	fun findShipByCaptain(captain: String): Iterable<Ship>
}

data class Ship(@Id val id: String? = null, val name: String, val captain: String)