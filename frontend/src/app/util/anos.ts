
export function gerarAnos(): number[] {
    var anoInicial: number = 1994
    const anoAtual = new Date().getFullYear();
    var anosGerados: number[] = []
    for (var i = anoInicial; i <= anoAtual; i++) {
      anosGerados.push(i);
    }
    return anosGerados.sort((a, b) => { return b - a });
  }