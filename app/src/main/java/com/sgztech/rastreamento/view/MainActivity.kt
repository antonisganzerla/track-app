package com.sgztech.rastreamento.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.api.RetrofitInitializer
import com.sgztech.rastreamento.model.PostalSearch
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.event_card_view.view.*
import kotlinx.android.synthetic.main.postal_object_card_view.*
import kotlinx.android.synthetic.main.postal_object_card_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSpinner()
        setupEtCode()
        setupFab()
        setupBtnTrack()
    }

    private fun setupBtnTrack() {
        btnTrack.setOnClickListener {
            hideKeyBoard()
            val code = etCode.text.toString()
            if (code.isEmpty()) {
                Snackbar.make(it, "Informe um código!", Snackbar.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.VISIBLE
                cardView.visibility = View.GONE
                sendRequest(code)
            }
        }
    }

    private fun sendRequest(code: String) {
        val call = RetrofitInitializer().service().findObject(code)
        call.enqueue(object : Callback<PostalSearch> {
            override fun onResponse(
                call: Call<PostalSearch>,
                response: Response<PostalSearch>
            ) {
                progressBar.visibility = View.GONE
                val postalSearch = response.body()
                if (postalSearch == null || postalSearch.objeto.isEmpty() || postalSearch.objeto[0].erro != null) {
                    if (postalSearch != null && postalSearch.objeto.isNotEmpty()) {
                        Snackbar.make(btnTrack, postalSearch.objeto[0].erro, Snackbar.LENGTH_LONG)
                            .show()
                    } else {
                        Snackbar.make(
                            btnTrack,
                            "OcorreU um problema ao buscar as informações",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else {
                    loadDataOnView(postalSearch)
                    Snackbar.make(
                        btnTrack,
                        "Informações recuperadas com sucesso",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PostalSearch>, t: Throwable) {
                progressBar.visibility = View.GONE
                Snackbar.make(btnTrack, "Falha ao buscar informações", Snackbar.LENGTH_LONG).show()
                Log.e("onFailure error", t.message)
            }
        })
    }

    private fun loadDataOnView(postalSearch: PostalSearch) {
        cardView.tvCategoria.text = postalSearch.objeto[0].categoria
        cardView.tvNome.text = postalSearch.objeto[0].nome
        cardView.tvSigla.text = postalSearch.objeto[0].sigla
        panelCard.removeAllViews()
        for (evento in postalSearch.objeto[0].evento) {
            val eventCardView = layoutInflater.inflate(R.layout.event_card_view, null)
            eventCardView.tvDescricao.text = evento.descricao
            eventCardView.tvData.text = evento.data.plus(" - ").plus(evento.hora)
            eventCardView.tvOrigem.text = evento.local
            eventCardView.tvDestino.text = evento.destino[0].local
            eventCardView.tvCidade.text = evento.destino[0].cidade
            eventCardView.tvUf.text = evento.destino[0].uf
            eventCardView.tvTipo.text = evento.tipo
            eventCardView.tvStatus.text = evento.status
            panelCard.addView(eventCardView)
        }
        cardView.visibility = View.VISIBLE
    }

    private fun setupFab() {
        fab.setOnClickListener {
            Snackbar.make(it, "fab", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupEtCode() {
        etCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // unnecessary implementation
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // unnecessary implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    spinnerCodes.setSelection(0)
                }
            }
        })
    }

    private fun setupSpinner() {
        spinnerCodes.adapter = ArrayAdapter(
            applicationContext,
            R.layout.code_list_item,
            listOf("--- Códigos ---", "LB083028640SG")
        )
        spinnerCodes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedCode = parent.getItemAtPosition(position).toString()
                if (selectedCode == "--- Códigos ---") {
                    etCode.setText("")
                } else {
                    etCode.setText(selectedCode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // unnecessary implementation
            }
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(btnTrack.windowToken, 0)
    }
}
