/*import './style.css'
import typescriptLogo from './typescript.svg'
import viteLogo from '/vite.svg'
import { setupCounter } from './counter.ts'

document.querySelector<HTMLDivElement>('#app')!.innerHTML = `
  <div>
    <a href="https://vite.dev" target="_blank">
      <img src="${viteLogo}" class="logo" alt="Vite logo" />
    </a>
    <a href="https://www.typescriptlang.org/" target="_blank">
      <img src="${typescriptLogo}" class="logo vanilla" alt="TypeScript logo" />
    </a>
    <h1>Vite + TypeScript</h1>
    <div class="card">
      <button id="counter" type="button"></button>
    </div>
    <p class="read-the-docs">
      Click on the Vite and TypeScript logos to learn more
    </p>
  </div>
`

setupCounter(document.querySelector<HTMLButtonElement>('#counter')!)*/

import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import LegacyApp from "./legacy/App";
import "./style.css";

const USE_LEGACY = false;

ReactDOM.createRoot(document.getElementById("app")!).render(
  <React.StrictMode>
    {USE_LEGACY ? <LegacyApp/> : <App />}
  </React.StrictMode>
);


