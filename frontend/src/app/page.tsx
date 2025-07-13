"use client";
import { useState } from "react";

export default function Home() {
  type TabsType =
    | "Detalhes da Conta"
    | "Registrar Conta"
    | "Transferencia"
    | "Rendimento de Juros";
  const [selectedTab, setSelectedTab] = useState<TabsType>("Detalhes da Conta");

  // CREATE ACCOUTN TAB FUNCTIONS
  const [createAccountNumber, setCreateAccountNumber] = useState<string>("");
  const [createAccountType, setCreateAccountType] = useState<string>("");
  const [createAccountInitialValue, setCreateAccountInitialValue] =
    useState<number>(0);
  const [createAccountMessage, setCreateAccountMessage] = useState<string>("");
  async function handleCreateAccountSubmit(
    const a
    event: React.FormEvent<HTMLFormElement>
  ) {
    event.preventDefault();
    try {
      const response = await fetch(
        "http://localhost:8080/api/conta/cadastrar",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            numeroConta: createAccountNumber,
            tipoConta: createAccountType,
            saldoInicial: createAccountInitialValue || 0,
          }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao criar conta");
      }
      setCreateAccountMessage("Conta criada com sucesso");
      setCreateAccountNumber("");
    } catch (error) {
      setCreateAccountMessage(
        error instanceof Error ? error.message : "Erro na requisicao"
      );
    }
  }

  // CONSULT ACCOUTN TAB FUNCTIONS
  const [consultAccountNumber, setConsultAccountNumber] = useState<string>("");
  const [detalhesConta, setDetalhesConta] = useState<{
    numeroConta: string | null;
    tipoConta: string | null;
    saldo: number;
    bonus: number | null;
  } | null>(null);
  const [consultAccountMessage, setConsultAccountMessage] =
    useState<string>("");
  async function handleConsultAccountSubmit(
    event: React.FormEvent<HTMLFormElement>
  ) {
    event.preventDefault();
    const target = event.nativeEvent as SubmitEvent;
    const submitter = target.submitter as HTMLButtonElement;
    const botaoClicado = submitter?.value;
    try {
      const response = await fetch(
        `http://localhost:8080/api/conta/${consultAccountNumber}/${botaoClicado}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao consultar conta");
      }
      const data = await response.json();
      if (botaoClicado == "dados") {
        setDetalhesConta({
          numeroConta: data.numeroConta,
          tipoConta: data.tipoConta,
          saldo: data.saldo,
          bonus: data.bonus || null,
        });
        setConsultAccountMessage(`Sucesso ao retornar dados da conta`);
      } else {
        setDetalhesConta({
          numeroConta: null,
          tipoConta: null,
          saldo: data.saldo,
          bonus: null,
        });
        setConsultAccountMessage(`Sucesso ao retornar saldo da conta`);
      }

      setConsultAccountNumber("");
    } catch (error) {
      setConsultAccountMessage(
        error instanceof Error ? error.message : "Erro na requisicao"
      );
    }
  }

  const [debitCreditAccountNumber, setdebitCreditAccountNumber] =
    useState<string>("");
  const [debitCreditValue, setdebitCreditValue] = useState<string>("");
  const [debitCreditAccountMessage, setDebitCreditAccountMessage] =
    useState<string>("");
  async function handleDebitCreditSubmit(
    event: React.FormEvent<HTMLFormElement>
  ) {
    event.preventDefault();
    const target = event.nativeEvent as SubmitEvent;
    const submitter = target.submitter as HTMLButtonElement;
    const botaoClicado = submitter?.value;
    try {
      const response = await fetch(
        `http://localhost:8080/api/conta/${debitCreditAccountNumber}/${botaoClicado}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            valor: debitCreditValue,
          }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(
          errorData.message || `Erro ${botaoClicado} valor da conta`
        );
      }
      setDebitCreditAccountMessage(
        `Valor ${
          botaoClicado == "debitar" ? "debitado" : "creditado"
        } com sucesso`
      );
      setdebitCreditAccountNumber("");
      setdebitCreditValue("");
    } catch (error) {
      setDebitCreditAccountMessage(
        error instanceof Error ? error.message : "Erro na requisicao"
      );
    }
  }

  // TRANSFER VALUE BETWEEN ACCOUNTS ACCOUTN TAB FUNCTIONS
  const [originAccountNumber, setOriginAccountNumber] = useState<string>("");
  const [destinAccountNumber, setDestinAccountNumber] = useState<string>("");
  const [transferValue, setTransferValue] = useState<string>("");
  const [transferValuetMessage, setTransferValueMessage] = useState<string>("");

  async function handleTransferSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    try {
      const response = await fetch(
        `http://localhost:8080/api/conta/${originAccountNumber}/transferir/${destinAccountNumber}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            valor: transferValue,
          }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(
          errorData.message || `Erro ao transferir valor da conta`
        );
      }
      setTransferValueMessage("Transferência realizada com sucesso");
      setOriginAccountNumber("");
      setDestinAccountNumber("");
      setTransferValue("");
    } catch (error) {
      setTransferValueMessage(
        error instanceof Error ? error.message : "Erro na requisicao"
      );
    }
  }

  // RENDIMENTO DE JUROS
  const [taxaJuros, setTaxaJuros] = useState<string>("");
  const [taxaJurosMessage, setTaxaJurosMessage] = useState<string>("");
  async function handleTaxaJurosSubmit(
    event: React.FormEvent<HTMLFormElement>
  ) {
    event.preventDefault();
    try {
      const response = await fetch(
        `http://localhost:8080/api/conta/render-juros-todas-as-contas`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            taxaDeJuros: taxaJuros,
          }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao aplicar taxa de juros");
      }
      setTaxaJurosMessage("Taxa de juros aplicada com sucesso");
      setTaxaJuros("");
    } catch (error) {
      setTaxaJurosMessage(
        error instanceof Error ? error.message : "Erro na requisicao"
      );
    }
  }

  return (
    <section className="flex-1 flex flex-col gap-2">
      <h1 className="text-center font-bold text-2xl">{selectedTab}</h1>

      <nav className="my-4">
        <ul className="flex justify-around space-x-4">
          {[
            "Detalhes da Conta",
            "Registrar Conta",
            "Transferencia",
            "Rendimento de Juros",
          ].map((tab) => (
            <li key={tab} className=" flex-1 w-full text-center">
              <button
                className={`${
                  selectedTab === tab ? "text-blue-600" : ""
                } cursor-pointer hover:underline text-lg font-medium`}
                onClick={() => setSelectedTab(tab as TabsType)}
              >
                {tab}
              </button>
            </li>
          ))}
        </ul>
      </nav>

      <section className="h-full grid place-items-center">
        {selectedTab === "Detalhes da Conta" && (
          <div className="flex gap-8 items-center">
            <form
              className="flex flex-col items-center gap-2"
              onSubmit={handleConsultAccountSubmit}
            >
              <label className="flex flex-col items-center gap-2">
                Numero da Conta:
                <input
                  type="text"
                  className="border border-gray-300 rounded p-2 min-w-[250px]"
                  placeholder="Digite o número da conta"
                  onChange={(e) => setConsultAccountNumber(e.target.value)}
                  value={consultAccountNumber}
                  required
                />
              </label>
              <div className="flex gap-2">
                <button
                  type="submit"
                  value="saldo"
                  className="bg-blue-500 text-white rounded p-2 hover:bg-blue-600 transition duration-200 cursor-pointer"
                >
                  Consultar Saldo
                </button>

                <button
                  type="submit"
                  value="dados"
                  className="bg-blue-500 text-white rounded p-2 hover:bg-blue-600 transition duration-200 cursor-pointer"
                >
                  Dados da Conta
                </button>
              </div>
              <span className="text-green-700 text-sm font-medium text-center block min-h-[20px]">
                {consultAccountMessage}
              </span>
              {detalhesConta && (
                <ul>
                  {detalhesConta.numeroConta && (
                    <li>
                      <strong>Numero da Conta: </strong>{" "}
                      {detalhesConta.numeroConta}
                    </li>
                  )}
                  {detalhesConta.tipoConta && (
                    <li>
                      <strong>Tipo da Conta: </strong> {detalhesConta.tipoConta}
                    </li>
                  )}
                  {detalhesConta.saldo && (
                    <li>
                      <strong>Saldo: </strong> {detalhesConta.saldo}
                    </li>
                  )}
                  {detalhesConta.bonus && (
                    <li>
                      <strong>Bonus: </strong> {detalhesConta.bonus}
                    </li>
                  )}
                </ul>
              )}
            </form>
            <form onSubmit={handleDebitCreditSubmit}>
              <label className="flex flex-col items-center gap-2 mb-1">
                Creditar/Debitar Valor na Conta:
                <input
                  type="text"
                  className="border border-gray-300 rounded p-2 min-w-[250px]"
                  placeholder="Digite o numero da conta"
                  onChange={(e) => setdebitCreditAccountNumber(e.target.value)}
                  value={debitCreditAccountNumber}
                  required
                />
                <input
                  type="text"
                  className="border border-gray-300 rounded p-2 min-w-[250px]"
                  placeholder="Digite o valor"
                  onChange={(e) => setdebitCreditValue(e.target.value)}
                  value={debitCreditValue}
                  required
                />
              </label>

              <div className="flex gap-2 justify-center mt-2">
                <button
                  type="submit"
                  value="debitar"
                  className="bg-red-600 text-white rounded p-2 hover:bg-red-700 transition duration-200 cursor-pointer"
                >
                  Debitar
                </button>
                <button
                  type="submit"
                  value="creditar"
                  className="bg-green-600 text-white rounded p-2 hover:bg-green-700 transition duration-200 cursor-pointer"
                >
                  Creditar
                </button>
              </div>
              <span className="text-green-700 text-sm font-medium text-center block min-h-[20px]">
                {debitCreditAccountMessage}
              </span>
            </form>
          </div>
        )}
        {selectedTab === "Registrar Conta" && (
          <div>
            <form
              className="flex flex-col items-center gap-2"
              onSubmit={handleCreateAccountSubmit}
            >
              <label className="flex flex-col items-center gap-2 mb-1">
                Numero da Conta:
                <input
                  type="text"
                  className="border border-gray-300 rounded p-2 min-w-[250px]"
                  placeholder="Digite o número da conta"
                  onChange={(e) => setCreateAccountNumber(e.target.value)}
                  value={createAccountNumber}
                  required
                />
              </label>

              <label className="flex flex-col items-center gap-2 mb-1">
                Tipo da conta:
                <select
                  className="border border-gray-300 rounded p-2 min-w-[250px]"
                  onChange={(e) => setCreateAccountType(e.target.value)}
                  value={createAccountType}
                  required
                >
                  <option value="" disabled>
                    Selecione o tipo de conta
                  </option>
                  <option value="SIMPLES">Simples</option>
                  <option value="BONUS">Bonus</option>
                  <option value="POUPANCA">Poupança</option>
                </select>
              </label>

              {(createAccountType == "POUPANCA" ||
                createAccountType == "SIMPLES") && (
                <label className="flex flex-col items-center gap-2 mb-1">
                  Valor inicial:
                  <input
                    type="number"
                    className="border border-gray-300 rounded p-2 min-w-[250px]"
                    placeholder="Digite o valor inicial da conta"
                    onChange={(e) =>
                      setCreateAccountInitialValue(Number(e.target.value))
                    }
                    value={createAccountInitialValue}
                    required
                  />
                </label>
              )}

              <button
                type="submit"
                className="bg-blue-500 text-white rounded p-2 hover:bg-blue-600 transition duration-200 cursor-pointer"
              >
                Criar Conta
              </button>
              <span className="text-green-700 text-sm font-medium text-center block min-h-[20px]">
                {createAccountMessage}
              </span>
            </form>
          </div>
        )}
        {selectedTab === "Transferencia" && (
          <div>
            <form
              onSubmit={handleTransferSubmit}
              className="flex flex-col items-center gap-2"
            >
              <label className="flex flex-col items-center gap-2 mb-1">
                Transferir valor entre contas
                <input
                  type="text"
                  className="border border-gray-300 rounded p-2 min-w-[280px]"
                  placeholder="Digite o numero da conta de origem"
                  onChange={(e) => setOriginAccountNumber(e.target.value)}
                  value={originAccountNumber}
                  required
                />
                <input
                  type="text"
                  className="border border-gray-300 rounded p-2 min-w-[280px]"
                  placeholder="Digite o numero da conta de destino"
                  onChange={(e) => setDestinAccountNumber(e.target.value)}
                  value={destinAccountNumber}
                  required
                />
                <input
                  type="text"
                  className="border border-gray-300 rounded p-2 min-w-[280px]"
                  placeholder="Digite o valor"
                  onChange={(e) => setTransferValue(e.target.value)}
                  value={transferValue}
                  required
                />
              </label>

              <button
                type="submit"
                className="bg-blue-500 text-white rounded p-2 hover:bg-blue-600 transition duration-200 cursor-pointer "
              >
                Transferir
              </button>
              <span className="text-green-700 text-sm font-medium text-center block min-h-[20px]">
                {transferValuetMessage}
              </span>
            </form>
          </div>
        )}

        {selectedTab === "Rendimento de Juros" && (
          <div>
            <form
              onSubmit={handleTaxaJurosSubmit}
              className="flex flex-col items-center gap-2"
            >
              <label className="flex flex-col items-center gap-2 mb-1">
                Informe a taxa de juros a ser aplicada a todas as contas
                <input
                  type="number"
                  className="border border-gray-300 rounded p-2 min-w-[280px]"
                  placeholder="Taxa de juros (%)"
                  onChange={(e) => setTaxaJuros(e.target.value)}
                  value={taxaJuros}
                  required
                />
              </label>

              <button
                type="submit"
                className="bg-blue-500 text-white rounded p-2 hover:bg-blue-600 transition duration-200 cursor-pointer "
              >
                Definir taxa de juros
              </button>
              <span className="text-green-700 text-sm font-medium text-center block min-h-[20px]">
                {taxaJurosMessage}
              </span>
            </form>
          </div>
        )}
      </section>
    </section>
  );
}
