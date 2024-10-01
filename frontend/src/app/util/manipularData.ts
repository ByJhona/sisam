import { formatInTimeZone} from "date-fns-tz/formatInTimeZone"
import {format} from "date-fns"
import {ptBR} from "date-fns/locale"



export function formatarAnoMesDia(data: Date): string {
  return format(data, "yyyy-MM-dd")
}

export function formatarMesAno(data: Date): string {
  console.log(data)
  return formatInTimeZone(data, "America/Sao_Paulo","MMM/yyyy", {locale:ptBR})
}