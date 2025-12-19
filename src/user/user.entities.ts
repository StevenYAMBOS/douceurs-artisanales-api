import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn('uuid')
  id: number;

  @Column({ unique: false })
  firstname: string;

  @Column({ unique: false })
  lastname: string;

  @Column({ unique: true })
  email: string;

  @Column()
  password_hash: string;
}
