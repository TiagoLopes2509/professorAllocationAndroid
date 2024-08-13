package com.example.professorallocation.repository

import android.util.Log
import com.example.professorallocation.model.Allocation
import com.example.professorallocation.model.AllocationwhithIds
import com.example.professorallocation.model.Professor
import com.example.professorallocation.service.AllocationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllocationRepository(private val allocationService: AllocationService) {

    fun saveAllocation(
        allocation: AllocationwhithIds,
        onSuccess: ()-> Unit,
        onError:(Throwable) -> Unit
    ){
        if(allocation.day == null || allocation.endHour == null || allocation.startHour == null || allocation.professorId == null || allocation.courseId == null) {
            onError(Throwable("Não pode conter campos em branco."))
            return
        }

        allocationService.save(allocation).enqueue(object : Callback<Any>{
            override fun onResponse(p0: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful){
                    onSuccess()
                }
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError(p1)
            }
        })
    }

    fun getAllocations(
        onSuccess: (List<Allocation>) -> Unit,
        onError: (String) -> Unit
    ) {
        allocationService.getAll().enqueue(object : Callback<List<Allocation>> {
            override fun onResponse(p0: Call<List<Allocation>>, response: Response<List<Allocation>>){
                if(response.isSuccessful){
                    val allocationsResponse = response.body()
                    if(allocationsResponse != null){
                        onSuccess(allocationsResponse)
                        Log.e("AllocationRepository", allocationsResponse.toString())
                    }else {
                        onError("Resposta vazia ou inválida ao carregar alocações.")
                    }
                } else {
                    onError("Erro ao carregar alocações: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<List<Allocation>>, p1: Throwable) {
                onError("Falha na requisição ao carregar alocações: ${p1.message}")
            }
        })
    }

    fun getAllocationById(
        id: Long,
        onSuccess: (allocation: Allocation?) -> Unit,
        onError: (message: String) -> Unit
    ){
        allocationService.getById(id).enqueue(object : Callback<Allocation> {
            override fun onResponse(p0: Call<Allocation>, response: Response<Allocation>) {
                response.isSuccessful.let {
                    if (it)
                        onSuccess(response.body())
                    else
                        onError(response.message())
                }
            }

            override fun onFailure(p0: Call<Allocation>, p1: Throwable) {
                p1.message?.let { onError(it) }
            }
        })
    }

    fun updateAllocation(
        allocation: AllocationwhithIds,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ){
        allocationService.update(allocation.id, allocation).enqueue(object : Callback<Any> {
            override fun onResponse(p0: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Throwable("Erro: ${response.code()} - ${response.message()}"))
                }
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError(p1)
            }

        })
    }

    fun deleteAllocation(
        id: Long,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        allocationService.delete(id).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if(response.isSuccessful){
                    onSuccess()
                } else {
                    onError("Falha ao deletar Professor: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<Any>, p1: Throwable) {
                onError("Error: ${p1.message}")
            }

        })
    }
}