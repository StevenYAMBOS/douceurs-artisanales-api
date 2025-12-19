import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: number;

  @Column({ unique: true, length: 100 })
  email: string;

  @Column({ unique: false, length: 50 })
  username: string;

  @Column({ length: 255 })
  password: string;

  @Column({ length: 20 })
  role: string;

  @Column()
  last_login: Date;

  @Column()
  created_at: Date;

  @Column()
  updated_at: Date;
}
