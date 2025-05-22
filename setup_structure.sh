#!/usr/bin/env bash
#
#  refactor_structure.sh
#  ----------------------
#  Porządkuje projekt JavaFX + Hibernate:
#    • tworzy katalogi
#    • przenosi pliki
#    • poprawia deklaracje ‘package’
#    • generuje proste DAO / Service, jeżeli brakuje
#
#  UŻYCIE:
#     chmod +x refactor_structure.sh
#     ./refactor_structure.sh           # w bieżącym katalogu
#     ./refactor_structure.sh demo      # lub w podkatalogu 'demo'
#
#  Wymagania: GNU sed (Linux) lub BSD sed (macOS) – skrypt sam wykryje wariant.

set -euo pipefail

PROJECT_DIR="${1:-.}"
BASE_JAVA="$PROJECT_DIR/src/main/java/com/example/demo"
BASE_RES="$PROJECT_DIR/src/main/resources"

echo "➤ Porządkowanie struktury w: $PROJECT_DIR"

# ---------- 1. katalogi ----------
for dir in entity dao service util seed ui container; do
  mkdir -p "$BASE_JAVA/$dir"
done
mkdir -p "$BASE_RES/META-INF"

# ---------- 2. mapowanie plików ----------
declare -A MAP=(
  [Teacher.java]=entity
  [TeacherCondition.java]=entity
  [ClassTeacher.java]=entity
  [Rate.java]=entity
  [ClassContainer.java]=container
  [HelloApplication.java]=ui
  [HelloController.java]=ui
  [TeacherSeed.java]=seed
)

# sed -i różni się między GNU a BSD – wykrycie wariantu
if sed --version >/dev/null 2>&1; then
  SED_INPLACE=("sed" "-i")
else
  SED_INPLACE=("sed" "-i" "")
fi

move_and_fix_package () {
  local src="$1" dst_dir="$2"
  local file="$(basename "$src")"
  local dst="$dst_dir/$file"
  mv "$src" "$dst"
  "${SED_INPLACE[@]}" "1s|^package .*;|package com.example.demo.${dst_dir##*/};|" "$dst"
  echo "  ✔ $file  → ${dst_dir##*/}/  (package zmienione)"
}

create_placeholder () {
  local dst_dir="$1" file="$2" class="${file%.java}"
  cat > "$dst_dir/$file" <<EOF
package com.example.demo.${dst_dir##*/};

public class $class {
    // TODO: implement
}
EOF
  echo "  ○ utworzono szablon: ${dst_dir##*/}/$file"
}

for file in "${!MAP[@]}"; do
  src="$BASE_JAVA/$file"
  dst_dir="$BASE_JAVA/${MAP[$file]}"
  if [[ -f "$src" ]]; then
    move_and_fix_package "$src" "$dst_dir"
  else
    create_placeholder "$dst_dir" "$file"
  fi
done

# ---------- 3. HibernateUtil ----------
if [[ ! -f "$BASE_JAVA/util/HibernateUtil.java" ]]; then
  cat > "$BASE_JAVA/util/HibernateUtil.java" <<'EOF'
package com.example.demo.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class HibernateUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("schoolPU");

    private HibernateUtil() { }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }
}
EOF
  echo "  ○ dodano util/HibernateUtil.java"
fi

# ---------- 4. DAO + Service place-holdery ----------
for cls in Teacher ClassTeacher Rate; do
  dao="$BASE_JAVA/dao/${cls}Dao.java"
  srv="$BASE_JAVA/service/${cls}Service.java"

  if [[ ! -f "$dao" ]]; then
    cat > "$dao" <<EOF
package com.example.demo.dao;

public class ${cls}Dao {
    // TODO: CRUD z użyciem EntityManagera
}
EOF
    echo "  ○ dao/${cls}Dao.java"
  fi

  if [[ ! -f "$srv" ]]; then
    cat > "$srv" <<EOF
package com.example.demo.service;

public class ${cls}Service {
    // TODO: logika biznesowa
}
EOF
    echo "  ○ service/${cls}Service.java"
  fi
done

# ---------- 5. persistence.xml ----------
PERSIST_SRC="$BASE_RES/com/example/demo/persistence.xml"
if [[ -f "$PERSIST_SRC" ]]; then
  mv "$PERSIST_SRC" "$BASE_RES/META-INF/persistence.xml"
  echo "  ✔ przeniesiono persistence.xml → META-INF/"
fi

echo "✅  Struktura uporządkowana – gotowe!"
