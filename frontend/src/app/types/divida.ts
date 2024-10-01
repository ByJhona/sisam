import { format } from "date-fns"

export interface Divida{
    valor: number,
    dataInicial: Date,
    dataFinal: Date
}


export function formatarData(data:Date):String {
    return format(data, "yyyy-MM-dd")
  }