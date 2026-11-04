# Vendas Demo Android

Aplicativo Android para simular notificações pessoais de vendas, sempre identificadas como demonstração. Não realiza vendas nem comprova recebimentos.

## Recursos
- Iniciar notificações automáticas com intervalo aleatório de 2 a 15 minutos.
- Botão **Testar notificação agora** para verificar imediatamente se o Android entrega notificações.
- Pausar notificações.
- Permissão de notificações solicitada em Android 13 ou superior.

## Gerar APK pelo GitHub
1. Envie todos os arquivos do projeto para a branch `main` do repositório. Inclua a pasta oculta `.github`.
2. Abra a aba **Actions** e execute **Build APK** (ou aguarde o workflow após o push).
3. Abra a execução concluída e baixe o artefato `VendasDemo-debug-apk`.
4. Extraia o ZIP para obter `app-debug.apk`.

## Testar
Instale o APK, abra o app e permita notificações. Toque primeiro em **Testar notificação agora**. Se essa notificação aparecer, toque em **Iniciar notificações** para iniciar os alarmes aleatórios.

Observação: alarmes não exatos podem ser atrasados pelo Android, especialmente em modo Doze/economia de bateria. O botão de teste envia a notificação imediatamente enquanto o app está aberto.
