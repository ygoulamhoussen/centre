import type { CliOption } from '../types'
import process from 'node:process'
import { loadConfig } from 'c12'

const defaultOptions: CliOption = {
  cwd: process.cwd(),
  cleanupDirs: [
    '**/dist',
    '**/package-lock.json',
    '**/yarn.lock',
    '**/pnpm-lock.yaml',
    '**/node_modules',
    '!node_modules/**',
  ],
  ncuCommandArgs: ['--deep', '-u'],
  changelogOptions: {},
}

export async function loadCliOptions(overrides?: Partial<CliOption>, cwd = process.cwd()) {
  const { config } = await loadConfig<Partial<CliOption>>({
    name: 'yussouf',
    defaults: defaultOptions,
    overrides,
    cwd,
    packageJson: true,
  })

  return config as CliOption
}
