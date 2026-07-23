import type { Category } from "../types/news";

const CATEGORIES: Category[] = [
  "FINANCE",
  "POLITICS",
  "HEALTHCARE",
  "TECHNOLOGY",
  "MILITARY",
  "MILITARY_INTELLIGENCE",
  "WORLD_POPULATION",
  "WORLD_ECONOMIES",
];

interface CategoryFilterProps {
  value: "ALL" | Category;
  onChange: (value: "ALL" | Category) => void;
  disabled: boolean;
}

export default function CategoryFilter({
  value,
  onChange,
  disabled,
}: CategoryFilterProps) {
  return (
    <label style={{ fontSize: 13, opacity: 0.8 }}>
      Category{" "}
      <select
        value={value}
        onChange={(e) =>
          onChange(e.target.value as "ALL" | Category)
        }
        style={{
          padding: "6px 8px",
          borderRadius: 8,
        }}
        disabled={disabled}
      >
        <option value="ALL">All</option>

        {CATEGORIES.map((category) => (
          <option key={category} value={category}>
            {category}
          </option>
        ))}
      </select>
    </label>
  );
}