import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule } from '@nestjs/config';
import { TypeOrmConfigService } from './config/database.service';
import { AuthModule } from './auth/auth.module';
import { UserModule } from './user/user.module';
import { JwtModule } from '@nestjs/jwt';
import { AUTH } from './config/constants';

@Module({
  imports: [
    ConfigModule.forRoot(),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      useClass: TypeOrmConfigService,
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
