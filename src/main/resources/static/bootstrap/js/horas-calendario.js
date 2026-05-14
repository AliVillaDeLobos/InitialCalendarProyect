document.addEventListener('DOMContentLoaded', () => {
    const calendarBody = document.getElementById('calendar-body');

    if(!calendarBody) {
        console.warn('No se encontró #calendar-body');
        return;
    }

    for (let h = 0; h <= 23; h++) {
        const tr = document.createElement('tr');

        // columna horas
        const hourTd = document.createElement('td');
        hourTd.textContent = `${h.toString().padStart(2, '0')}:00`;
        hourTd.className = 'text-end align-middle';
        tr.appendChild(hourTd);

        // columnas de días
        for (let d = 0; d < 7; d++) {
            const td = document.createElement('td');
            td.style.height = '60px';
            td.style.textAlign = 'center';
            td.style.verticalAlign = 'middle';
            td.style.padding = '4px';

            // Todas las subtareas para esta celda
            const tareas = (window.subtareasFormateadas || []).filter(t => t.dia === d && t.hora === h);

            if (tareas.length > 0) {
                // Pintar toda la celda con el color de la primera subtarea
                td.style.backgroundColor = tareas[0].colorClase;
                td.style.color = '#000000';
                td.textContent = tareas.map(t => t.nombreSubtarea).join(', ');

                // Añadir listener para mostrar detalles de la PRIMERA subtarea de esa celda
                td.addEventListener('click', () => {
                    // puedes cambiar a mostrar una lista de opciones si hay varias
                    mostrarDetallesSubtarea(tareas[0].idSubtarea);
                });
            }
            /*tareas.forEach(t => {
                const form = document.createElement('form');
                form.method = 'post';
                form.action = '/auth/inicio/seleccionar-subtarea';
                form.style.display = 'inline';

                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'idSubtarea';
                input.value = t.idSubtarea;
                form.appendChild(input);

                const button = document.createElement('button');
                button.type = 'submit';
                button.style.all = 'unset';
                button.style.cursor = 'pointer';
                button.style.display = 'block';
                button.style.width = '100%';
                button.style.height = '100%';
                button.textContent = t.nombreSubtarea;
                button.style.backgroundColor = t.colorClase;
                button.style.color = '#fff';
                button.style.padding = '2px';
                button.style.borderRadius = '4px';
                form.appendChild(button);

                td.appendChild(form);
            });*/

            tr.appendChild(td);
        }

        calendarBody.appendChild(tr);
    }
});