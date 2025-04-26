import enUS from './langs/en-us'
import viVN from './langs/vi-vn'
import fr from './langs/fr'

const locales: Record<App.I18n.LangType, App.I18n.Schema> = {
  'vi-VN': viVN,
  'en-US': enUS,
  'fr': fr,
}

export default locales
