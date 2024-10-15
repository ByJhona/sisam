import { Indice } from "./Indice";

export class DividaCalculada {
    valorInicial: number
    valorFinal: number
    dataInicial: Date
    dataFinal: Date
    indices: Indice[]
    constructor(valorInicial: number = 0,
        valorFinal: number = 0,
        dataInicial: Date = new Date(1500, 0, 0, 0, 0),
        dataFinal: Date = new Date(1500, 0, 0, 0, 0),
        indices: Indice[] = []) {
        this.valorInicial = valorInicial
        this.valorFinal = valorFinal
        this.dataInicial = dataInicial
        this.dataFinal = dataFinal
        this.indices = indices
    }

    getValorInicial(){
        return this.valorInicial.toLocaleString("pt-BR");
    }

    getValorFinal(){
        return this.valorFinal.toLocaleString("pt-BR");
    }


}

