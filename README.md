# Vendas Demo (Android)

Projeto Android nativo para notificações pessoais fictícias. Todas as notificações identificam explicitamente que são uma **DEMONSTRAÇÃO** e que nenhuma transação real ocorreu.

## Gerar APK pelo GitHub
1. Crie um repositório novo no GitHub.
2. Envie todos os arquivos desta pasta para a raiz do repositório (incluindo `.github`).
3. Abra a aba **Actions** e execute **Build APK** (ou aguarde o build após enviar para `main`).
4. Quando terminar, abra a execução concluída e baixe o artifact **VendasDemo-debug-apk**.
5. Extraia o ZIP do artifact e instale `app-debug.apk` no Android. Talvez seja necessário autorizar a instalação pelo navegador/gerenciador de arquivos.

## Uso
- Abra **Vendas Demo** e toque em **Iniciar notificações**.
- Autorize as notificações quando o Android perguntar.
- As notificações aparecem em intervalos aleatórios de 2 a 15 minutos, com valores fictícios.
- Toque em **Pausar notificações** para cancelar o próximo aviso.

## Limitações do Android
O agendamento usa alarmes não exatos para respeitar as restrições modernas do Android. Economia de bateria, modo Doze e fabricantes podem atrasar notificações. O Android pode cancelar alarmes após reiniciar o aparelho; nesse caso, abra o app e inicie novamente. Forçar a parada do app também impede novos avisos até abri-lo novamente.
