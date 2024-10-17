import pandas as pd
pd.set_option('display.precision', 15)  # Ajuste o valor conforme necessário
df = pd.read_excel(open("tabelas.xlsx", 'rb'), sheet_name='Tabela_CM_Juros' ,nrows=28, skiprows=37)


def comparar(data, valor):
    banco = pd.read_csv(open('fatores_indices.csv', 'rb'))
    print(banco)
    
    for i, elemento in banco.iterrows():
        for mes,valor in elemento.items():
            data = definirData(ano, mes)
            if data !=  '':
            sql = f"insert into fatores_indices (data, valor) values('{data}', {valor});"
            comparar(data, valor)
def definirData(ano, m):
    meses = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto","Setembro", "Outubro", "Novembro", "Dezembro"]
    ano = int(ano)
    for i,mes in enumerate(meses):
        i += 1
        i = str(i).zfill(2)
        if(m == mes):
            return f"{ano}-{i}-01"
    return ''


for i, elemento in df.iterrows():
    ano = elemento.values[0]
    for mes,valor in elemento.items():
        data = definirData(ano, mes)
        if data !=  '':
            sql = f"insert into fatores_indices (data, valor) values('{data}', {valor});"
            comparar(data, valor)
        

