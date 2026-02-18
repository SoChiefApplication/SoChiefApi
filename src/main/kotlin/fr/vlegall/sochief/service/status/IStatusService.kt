package fr.vlegall.sochief.service.status

import fr.vlegall.sochief.contracts.response.ApiStatus

interface IStatusService {
    fun getStatus(): ApiStatus
}