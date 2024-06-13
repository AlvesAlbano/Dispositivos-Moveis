package com.example.appespacoculturalkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import android.app.AlertDialog

class HorarioAdapter(private val horarios: List<HorarioItem>) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.horarios_item, parent, false)
        return HorarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val currentItem = horarios[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = horarios.size

    class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val diaSemanaTextView: TextView = itemView.findViewById(R.id.dia_semana)
        private val horaInicioTextView: TextView = itemView.findViewById(R.id.hora_inicio)
        private val horaTerminoTextView: TextView = itemView.findViewById(R.id.hora_termino)
        private val horaInteresseTextView: TextView = itemView.findViewById(R.id.hora_interesse)
        private val db = FirebaseFirestore.getInstance()

        fun bind(horarioItem: HorarioItem) {
            diaSemanaTextView.text = horarioItem.diaSemana
            horaInicioTextView.text = horarioItem.horaInicio
            horaTerminoTextView.text = horarioItem.horaTermino
            horaInteresseTextView.text = "${horarioItem.pessoasInteressadas} Pessoas Interessadas"

            // Verifica o estado do interesse e define o listener de clique apropriado
            if (!horarioItem.interesseConfirmado) {
                itemView.setOnClickListener {
                    showConfirmDialog(horarioItem)
                }
            } else {
                itemView.setOnClickListener {
                    showUnconfirmDialog(horarioItem)
                }
            }
        }

        private fun showConfirmDialog(horarioItem: HorarioItem) {
            val context = itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.confirmar_interesse, null)
            val dialogMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
            dialogMessage.text = "Confirma o interesse no horário de ${horarioItem.diaSemana}?"

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            dialogView.findViewById<View>(R.id.button_confirm).setOnClickListener {
                horarioItem.interesseConfirmado = true
                incrementInteresse(horarioItem)
                dialog.dismiss()
                // Remove o listener de clique após a confirmação
                itemView.setOnClickListener {
                    showUnconfirmDialog(horarioItem)
                }
            }

            dialogView.findViewById<View>(R.id.button_cancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun showUnconfirmDialog(horarioItem: HorarioItem) {
            val context = itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.desconfirmar_interesse, null)
            val dialogMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
            dialogMessage.text = "Deseja desconfirmar o interesse no horário de ${horarioItem.diaSemana}?"

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            dialogView.findViewById<View>(R.id.button_confirm).setOnClickListener {
                horarioItem.interesseConfirmado = false
                decrementInteresse(horarioItem)
                dialog.dismiss()
                // Remove o listener de clique após a desconfirmação
                itemView.setOnClickListener {
                    showConfirmDialog(horarioItem)
                }
            }

            dialogView.findViewById<View>(R.id.button_cancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun incrementInteresse(horarioItem: HorarioItem) {
            val diaSemana = horarioItem.diaSemana
            val contadorKey = "contador$diaSemana"

            db.collection("Horarios").document(diaSemana)
                .update(contadorKey, FieldValue.increment(1))
                .addOnSuccessListener {
                    db.collection("Horarios").document(diaSemana).get()
                        .addOnSuccessListener { document ->
                            val updatedCount = document.getLong(contadorKey) ?: horarioItem.pessoasInteressadas
                            horarioItem.pessoasInteressadas = updatedCount
                            horaInteresseTextView.text = "${horarioItem.pessoasInteressadas} Pessoas Interessadas"
                        }
                }
                .addOnFailureListener { e ->
                    // Trate falhas de atualização aqui, se necessário
                }
        }

        private fun decrementInteresse(horarioItem: HorarioItem) {
            val diaSemana = horarioItem.diaSemana
            val contadorKey = "contador$diaSemana"

            db.collection("Horarios").document(diaSemana)
                .update(contadorKey, FieldValue.increment(-1))
                .addOnSuccessListener {
                    db.collection("Horarios").document(diaSemana).get()
                        .addOnSuccessListener { document ->
                            val updatedCount = document.getLong(contadorKey) ?: horarioItem.pessoasInteressadas
                            horarioItem.pessoasInteressadas = updatedCount
                            horaInteresseTextView.text = "${horarioItem.pessoasInteressadas} Pessoas Interessadas"
                        }
                }
                .addOnFailureListener { e ->
                    // Trate falhas de atualização aqui, se necessário
                }
        }
    }
}