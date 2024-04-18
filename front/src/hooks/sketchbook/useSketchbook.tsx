import { useQuery, useQueryClient, useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import {
  getSketchbookSelected,
  putSketchbookName,
  deleteSketchbook,
} from "../../api/Api";
import { Page_Url } from "../../router/Page_Url";

interface IProps {
  sketchbookId: number;
  name: string;
}

/** 스케치북 불러오기 */
export default function useSketchbook(sketchbookId: number) {
  return useQuery({
    queryKey: ["sketchbook"],
    queryFn: () => getSketchbookSelected(sketchbookId),
  });
}

/** 스케치북 수정  */
export function usePutSketchbook() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      sketchbookId,
      name,
    }: {
      sketchbookId: number;
      name: string;
    }) => putSketchbookName(sketchbookId, name),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["sketchbook"] });
    },
  });
}

/** 스케치북 삭제  */
export function useDeleteSketchbook() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (sketchbookId: number) => deleteSketchbook(sketchbookId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["sketchbook"] });
      navigate(Page_Url.SketchbookList);
    },
  });
}
