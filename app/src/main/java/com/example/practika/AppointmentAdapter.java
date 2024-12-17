package com.example.practika;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;

    public AppointmentAdapter(List<Appointment> appointmentList, Context context) {
        this.appointmentList = appointmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.textName.setText(appointment.getName());
        holder.textPhone.setText(appointment.getPhone());
        holder.textService.setText(appointment.getService());
        holder.textDate.setText(appointment.getDate());
        holder.textTime.setText(appointment.getTime());
        holder.textSpecialist.setText(appointment.getSpecialist());

        // Удаление записи
        holder.btnDelete.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(context);
            if (dbHelper.deleteAppointment(position + 1)) { // Пример: ID записи берется по позиции + 1
                appointmentList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Запись отменена", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Ошибка при отмене записи", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textPhone, textService, textDate, textTime, textSpecialist;
        Button btnDelete;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textPhone = itemView.findViewById(R.id.text_phone);
            textService = itemView.findViewById(R.id.text_service);
            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
            textSpecialist = itemView.findViewById(R.id.text_specialist);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}