import{z as b,A as u,B as g,C as j,d as y,D as _,E as w,H as P,I as D,J as $,L as H,a as k,M as O,N as M,O as A,P as F,Q as K,R as U,S as G,U as J,j as Q,t as X,V as Y,W as Z,c as h,f as B,g as f,o as d,k as C,l as R,b as z,e as q,y as S,X as W,Y as ee,F as L,Z as re}from"./index-D_-DzWhp.js";import{_ as te}from"./Dropdown-BfCXARAG.js";const ne=b("breadcrumb",`
 white-space: nowrap;
 cursor: default;
 line-height: var(--n-item-line-height);
`,[u("ul",`
 list-style: none;
 padding: 0;
 margin: 0;
 `),u("a",`
 color: inherit;
 text-decoration: inherit;
 `),b("breadcrumb-item",`
 font-size: var(--n-font-size);
 transition: color .3s var(--n-bezier);
 display: inline-flex;
 align-items: center;
 `,[b("icon",`
 font-size: 18px;
 vertical-align: -.2em;
 transition: color .3s var(--n-bezier);
 color: var(--n-item-text-color);
 `),u("&:not(:last-child)",[j("clickable",[g("link",`
 cursor: pointer;
 `,[u("&:hover",`
 background-color: var(--n-item-color-hover);
 `),u("&:active",`
 background-color: var(--n-item-color-pressed); 
 `)])])]),g("link",`
 padding: 4px;
 border-radius: var(--n-item-border-radius);
 transition:
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 color: var(--n-item-text-color);
 position: relative;
 `,[u("&:hover",`
 color: var(--n-item-text-color-hover);
 `,[b("icon",`
 color: var(--n-item-text-color-hover);
 `)]),u("&:active",`
 color: var(--n-item-text-color-pressed);
 `,[b("icon",`
 color: var(--n-item-text-color-pressed);
 `)])]),g("separator",`
 margin: 0 8px;
 color: var(--n-separator-color);
 transition: color .3s var(--n-bezier);
 user-select: none;
 -webkit-user-select: none;
 `),u("&:last-child",[g("link",`
 font-weight: var(--n-font-weight-active);
 cursor: unset;
 color: var(--n-item-text-color-active);
 `,[b("icon",`
 color: var(--n-item-text-color-active);
 `)]),g("separator",`
 display: none;
 `)])])]),T=M("n-breadcrumb"),oe=Object.assign(Object.assign({},P.props),{separator:{type:String,default:"/"}}),ae=y({name:"Breadcrumb",props:oe,setup(e){const{mergedClsPrefixRef:n,inlineThemeDisabled:o}=w(e),a=P("Breadcrumb","-breadcrumb",ne,D,e,n);$(T,{separatorRef:H(e,"separator"),mergedClsPrefixRef:n});const c=k(()=>{const{common:{cubicBezierEaseInOut:m},self:{separatorColor:l,itemTextColor:s,itemTextColorHover:i,itemTextColorPressed:p,itemTextColorActive:v,fontSize:r,fontWeightActive:x,itemBorderRadius:N,itemColorHover:E,itemColorPressed:I,itemLineHeight:V}}=a.value;return{"--n-font-size":r,"--n-bezier":m,"--n-item-text-color":s,"--n-item-text-color-hover":i,"--n-item-text-color-pressed":p,"--n-item-text-color-active":v,"--n-separator-color":l,"--n-item-color-hover":E,"--n-item-color-pressed":I,"--n-item-border-radius":N,"--n-font-weight-active":x,"--n-item-line-height":V}}),t=o?O("breadcrumb",void 0,c,e):void 0;return{mergedClsPrefix:n,cssVars:o?void 0:c,themeClass:t==null?void 0:t.themeClass,onRender:t==null?void 0:t.onRender}},render(){var e;return(e=this.onRender)===null||e===void 0||e.call(this),_("nav",{class:[`${this.mergedClsPrefix}-breadcrumb`,this.themeClass],style:this.cssVars,"aria-label":"Breadcrumb"},_("ul",null,this.$slots))}});function se(e=U?window:null){const n=()=>{const{hash:c,host:t,hostname:m,href:l,origin:s,pathname:i,port:p,protocol:v,search:r}=(e==null?void 0:e.location)||{};return{hash:c,host:t,hostname:m,href:l,origin:s,pathname:i,port:p,protocol:v,search:r}},o=A(n()),a=()=>{o.value=n()};return F(()=>{e&&(e.addEventListener("popstate",a),e.addEventListener("hashchange",a))}),K(()=>{e&&(e.removeEventListener("popstate",a),e.removeEventListener("hashchange",a))}),o}const ie={separator:String,href:String,clickable:{type:Boolean,default:!0},onClick:Function},ce=y({name:"BreadcrumbItem",props:ie,slots:Object,setup(e,{slots:n}){const o=G(T,null);if(!o)return()=>null;const{separatorRef:a,mergedClsPrefixRef:c}=o,t=se(),m=k(()=>e.href?"a":"span"),l=k(()=>t.value.href===e.href?"location":null);return()=>{const{value:s}=c;return _("li",{class:[`${s}-breadcrumb-item`,e.clickable&&`${s}-breadcrumb-item--clickable`]},_(m.value,{class:`${s}-breadcrumb-item__link`,"aria-current":l.value,href:e.href,onClick:e.onClick},n),_("span",{class:`${s}-breadcrumb-item__separator`,"aria-hidden":"true"},J(n.separator,()=>{var i;return[(i=e.separator)!==null&&i!==void 0?i:a.value]})))}}}),le={class:"i-flex-y-center align-middle"},me=y({name:"GlobalBreadcrumb",__name:"index",setup(e){const n=Q(),o=X(),{routerPushByKey:a}=Y(),[c,t]=Z(),m=l=>a(l);return(l,s)=>{const i=te,p=ce,v=ae;return f(n).header.breadcrumb.visible?(d(),h(v,{key:0},{default:C(()=>[R(f(c),null,{default:C(({breadcrumb:r})=>[q("div",le,[f(n).header.breadcrumb.showIcon?(d(),h(S(r.icon),{key:0,class:"mr-4px text-icon"})):B("",!0),typeof r.label=="string"?(d(),z(L,{key:1},[W(ee(r.label),1)],64)):(d(),h(S(r.label()),{key:2}))])]),_:1}),(d(!0),z(L,null,re(f(o).breadcrumbs,r=>(d(),h(p,{key:r.key},{default:C(()=>{var x;return[(x=r.options)!=null&&x.length?(d(),h(i,{key:0,options:r.options,onSelect:m},{default:C(()=>[R(f(t),{breadcrumb:r},null,8,["breadcrumb"])]),_:2},1032,["options"])):(d(),h(f(t),{key:1,breadcrumb:r},null,8,["breadcrumb"]))]}),_:2},1024))),128))]),_:1})):B("",!0)}}});export{me as _};
