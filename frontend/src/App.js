import React, { useState } from 'react';
import './App.css';
import Patients from './Patients';
import Doctors from './Doctors';
import Appointments from './Appointments';
import Billing from './Billing';

function App() {
  const [active, setActive] = useState('patients');

  return (
    <div className="app">
      <div className="sidebar">
        <h2 className="logo">🏥 HMS</h2>
        <button className={active === 'patients' ? 'active' : ''}
          onClick={() => setActive('patients')}>🧑 Patients</button>
        <button className={active === 'doctors' ? 'active' : ''}
          onClick={() => setActive('doctors')}>👨 Doctors</button>
        <button className={active === 'appointments' ? 'active' : ''}
          onClick={() => setActive('appointments')}>📅 Appointments</button>
        <button className={active === 'billing' ? 'active' : ''}
          onClick={() => setActive('billing')}>💰 Billing</button>
      </div>
      <div className="content">
        {active === 'patients'     && <Patients />}
        {active === 'doctors'      && <Doctors />}
        {active === 'appointments' && <Appointments />}
        {active === 'billing'      && <Billing />}
      </div>
    </div>
  );
}

export default App;