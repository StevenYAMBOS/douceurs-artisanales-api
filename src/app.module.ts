import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule, ConfigService } from '@nestjs/config';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        type: 'postgres',
        host: configService.get<string>(process.env.DATABASE_HOST),
        port: +configService.get<number>('DB_PORT', 5432),
        username: configService.get<string>(process.env.DATABASE_USERNAME),
        password: configService.get<string>(process.env.DATABASE_PASSWORD),
        database: configService.get<string>(process.env.DATABASE_NAME),
        autoLoadEntities: true,
        synchronize: true, // (⚠️ Mettre en 'false' en prod !)
      }),
    }),
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
