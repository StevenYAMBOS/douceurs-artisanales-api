import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { APP_CONFIG } from './config/constants';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  await app.listen(APP_CONFIG.SERVER_PORT);
}
bootstrap();
