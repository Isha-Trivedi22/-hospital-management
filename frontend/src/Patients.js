import React, { useState, useEffect } from 'react';
import API from './api';

function Patients() {
  const [patients, setPatients] = useState([]);
  const [form, setForm] = useState({
    name: '', age: '', gender: '', phone: '', disease: ''
  });

  useEffect(() => { fetchPatients(); }, []);

  const fetchPatients = async () => {
    try {
      const res = await API.get('/patients');
      setPatients(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const addPatient = async () => {
    if (!form.name || !form.age) return alert('Name and Age required!');
    try {
      await API.post('/patients', { ...form, age: parseInt(form.age) });
      setForm({ name: '', age: '', gender: '', phone: '', disease: '' });
      fetchPatients();
    } catch (err) {
      console.error(err);
    }
  };

  const deletePatient = async (id) => {
    await API.delete(`/patients/${id}`);
    fetchPatients();
  };

  return (
    <div>
      <h2>🧑 Patient Management</h2>
      <div className="form-row">
        <input placeholder="Name" value={form.name}
          onChange={e => setForm({...form, name: e.target.value})} />
        <input placeholder="Age" value={form.age} type="number"
          onChange={e => setForm({...form, age: e.target.value})} />
        <input placeholder="Gender" value={form.gender}
          onChange={e => setForm({...form, gender: e.target.value})} />
        <input placeholder="Phone" value={form.phone}
          onChange={e => setForm({...form, phone: e.target.value})} />
        <input placeholder="Disease" value={form.disease}
          onChange={e => setForm({...form, disease: e.target.value})} />
        <button className="btn-add" onClick={addPatient}>➕ Add</button>
      </div>
      <table>
        <thead>
          <tr><th>ID</th><th>Name</th><th>Age</th>
              <th>Gender</th><th>Phone</th><th>Disease</th><th>Action</th></tr>
        </thead>
        <tbody>
          {patients.map(p => (
            <tr key={p.id}>
              <td>{p.id}</td><td>{p.name}</td><td>{p.age}</td>
              <td>{p.gender}</td><td>{p.phone}</td><td>{p.disease}</td>
              <td><button className="btn-delete"
                onClick={() => deletePatient(p.id)}>🗑 Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Patients;