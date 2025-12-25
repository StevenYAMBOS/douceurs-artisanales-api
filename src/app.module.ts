import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule } from '@nestjs/config';
import { TypeOrmConfigService } from './config/database.service';
import { AuthModule } from './auth/auth.module';
import { UserModule } from './user/user.module';
import { JwtModule } from '@nestjs/jwt';
import { ThrottlerModule } from '@nestjs/throttler';
import { APP_CONFIG, AUTH } from './config/constants';

@Module({
  imports: [
    ConfigModule.forRoot({ envFilePath: 'env', isGlobal: true }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      useClass: TypeOrmConfigService,
    }),
    ThrottlerModule.forRoot({
      throttlers: [
        {
          ttl: APP_CONFIG.RATE_LIMIT_WINDOW_MS,
          limit: APP_CONFIG.MAX_REQUEST_TIMEOUT_MS,
        },
      ],
    }),
    AuthModule,
    UserModule,
    JwtModule.register({
      global: true,
      secret: AUTH.JWT_SECRET_KEY,
      signOptions: { expiresIn: AUTH.EXPIRATION_TIME },
    }),
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
